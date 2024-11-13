package com.example.demo.serviceImp;

import com.example.demo.model.Bill;
import com.example.demo.model.DetailBill;
import com.example.demo.model.Message;
import com.example.demo.model.Product;
import com.example.demo.payload.request.bill.BillRequest;
import com.example.demo.payload.request.bill.DetailBillRequest;
import com.example.demo.payload.response.BillResponse;
import com.example.demo.payload.response.DetailBillResponse;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.DetailBillRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticateService;
import com.example.demo.service.BillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BillServiceImp implements BillService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private DetailBillRepository detailBillRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ResponseEntity<?> addNew(BillRequest billRequest, HttpServletRequest request) {
        Long storeId = authenticateService.getStoreIdByUserId(request);
        Long saleId = authenticateService.getUserIdByToken(request);
        if (storeId == null) {
            //logger
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.STORE_NOT_FOUND);
        }
        if (saleId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.USER_NOT_FOUND);
        }
        if (billRequest.getDetailBill() == null && billRequest.getDetailBill().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.DETAIL_BILL_NOT_FOUND);
        }
        List<Product> productListToBill = new ArrayList<>();
        Map<Product, Integer> productQuantityMap = new HashMap<>();
        for (DetailBillRequest detailBillRequest : billRequest.getDetailBill()) {
            Product product = productRepository.findByIdAndStore(Long.valueOf(detailBillRequest.getProductId()),storeId).get();
            if (product == null) {
                //logger
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.PRODUCT_NOT_FOUND);
            }
            productListToBill.add(product);
            productQuantityMap.put(product, detailBillRequest.getQuantity());
        }

        Bill bill = new Bill();
        saveBill(bill, billRequest, storeId, saleId, productListToBill);
        bill = billRepository.save(bill);
        saveBillDetail(productListToBill,bill,productQuantityMap);

        return ResponseEntity.status(HttpStatus.OK).body(Message.CREATE_BILL_SUCCESS);
    }

    private void saveBill(Bill bill, BillRequest billRequest, Long storeId, Long saleId, List<Product> productList){
        LocalDateTime today = LocalDateTime.now();
        bill.setSellDate(today);
        bill.setNotes(billRequest.getNotes());
        bill.setOrderStatus(1);
        BigDecimal bigDecimalValue = new BigDecimal(billRequest.getCustomerPay());
        bill.setCustomerPay(bigDecimalValue);
        bill.setStoreId(storeId);
        bill.setSaleId(saleId);
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Product product : productList) {
            BigDecimal prdSellPrice = product.getPrdSellPrice();

            totalPrice = totalPrice.add(prdSellPrice);
        }
        bill.setTotalPrice(totalPrice);
        bill.setTotalQuantity(productList.size());
        bill.setChangeAmount(bill.getCustomerPay().subtract(totalPrice));
    }

    private void saveBillDetail (List<Product> productList,Bill bill,Map<Product, Integer> productQuantityMap) {
        for (Product product : productList) {
            DetailBill detailBill = new DetailBill();
            detailBill.setBillId(bill.getBillId());
            detailBill.setProductId(product.getProductId());
            detailBill.setQuantity(productQuantityMap.get(product));
            detailBill.setPrice(product.getPrdSellPrice().multiply(new BigDecimal(detailBill.getQuantity())));
            detailBillRepository.save(detailBill);
        }
    }

    @Override
    public ResponseEntity<?> getBillList(HttpServletRequest request, Pageable pageable) {
        Long userId = authenticateService.getUserIdByToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.USER_NOT_FOUND);
        }

        Long storeId = authenticateService.getStoreIdByUserId(request);
        if (storeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.STORE_NOT_FOUND);
        }
        Page<Bill> billPage = billRepository.findByStoreId(storeId, pageable);

        if (billPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No bills found");
        }

        List<DetailBill> allDetailBills = detailBillRepository.findByBillIdIn(billPage.stream().collect(Collectors.toList()).stream()
                        .map(Bill::getBillId)
                        .collect(Collectors.toList())).stream()
                .collect(Collectors.toList());

        Map<Long, List<DetailBill>> detailBillMap = allDetailBills.stream()
                .collect(Collectors.groupingBy(DetailBill::getBillId));
        //allDetailBills co danh sach cac detail bill va dung groupingBy de map cac detail bill co dung billId voi nhau vao 1 map

        Map<Long, Product> productMap = productRepository.findByProductIdInAndStoreId(allDetailBills.stream()
                        .map(DetailBill::getProductId)
                        .distinct()
                        .collect(Collectors.toList()), storeId)
                .stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));

        List<BillResponse> billResponseList = billPage.stream().map(bill -> {
            BillResponse billResponse = BillResponse.builder()
                    .billId(bill.getBillId())
                    .sellDate(bill.getSellDate().format(formatter))
                    .saleName(userRepository.getUserNameById(bill.getSaleId()) == null ? "" : userRepository.getUserNameById(bill.getSaleId()))
                    .notes(bill.getNotes())
                    .totalQuantity(bill.getTotalQuantity())
                    .totalPrice(bill.getTotalPrice())
                    .customerPay(bill.getCustomerPay())
                    .changeAmount(bill.getChangeAmount())
                    .orderStatus(bill.getOrderStatus())
                    .build();

            List<DetailBillResponse> detailBillResponseList = new ArrayList<>();

            detailBillMap.getOrDefault(bill.getBillId(), Collections.emptyList()).forEach(detailBill -> {
                DetailBillResponse detailBillResponse = DetailBillResponse.builder()
                        .detailBillId(detailBill.getDetailBillId())
                        .productId(detailBill.getProductId())
                        .productName(productMap.getOrDefault(detailBill.getProductId(), new Product()).getProductName())
                        .quantity(detailBill.getQuantity())
                        .price(detailBill.getPrice())
                        .build();
                detailBillResponseList.add(detailBillResponse);
            });

            billResponse.setDetailBill(detailBillResponseList);
            return billResponse;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(new PageImpl<>(billResponseList, pageable, billPage.getTotalElements()));
    }

    @Override
    public ResponseEntity<?> getRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = billRepository.calculateRevenueBetweenDates(startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK).body(totalPrice);
    }

    @Override
    public ResponseEntity<?> getRevenueByStore(Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = billRepository.calculateRevenueByStore(storeId, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(totalPrice);
    }

    @Override
    public ResponseEntity<?> getRevenueForAllStores(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = billRepository.calculateRevenueForAllStores(startDate, endDate);
        Map<Long, BigDecimal> revenueByStore = new HashMap<>();
        for (Object[] result : results) {
            Long storeId = (Long) result[0];
            BigDecimal revenue = (BigDecimal) result[1];
            revenueByStore.put(storeId, revenue);
        }
        return ResponseEntity.status(HttpStatus.OK).body(revenueByStore);
    }

}
