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
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Long saleId = authenticateService.getUserIdByToken(request);
        if (saleId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.USER_NOT_FOUND));
        }
        if (billRequest.getDetailBill() == null || billRequest.getDetailBill().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.DETAIL_BILL_NOT_FOUND));
        }
        Bill bill = new Bill();
        List<Product> productListToBill = new ArrayList<>();
        Map<Product, Integer> productQuantityMap = new HashMap<>();
        for (DetailBillRequest detailBillRequest : billRequest.getDetailBill()) {
            try {
                Long productId = detailBillRequest.getProductId(); // Nếu detailBillRequest trả về String
                Optional<Product> productOptional = productRepository.findById(productId);

                if (productOptional.isEmpty()) {
                    // Log thông tin nếu cần
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.PRODUCT_NOT_FOUND));
                }

                Product product = productOptional.get();
                productListToBill.add(product);
                productQuantityMap.put(product, detailBillRequest.getQuantity());
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu productId không phải là số
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.map("Invalid Product ID format"));
            }
        }

        saveBill(bill, billRequest, saleId, productQuantityMap);
        bill = billRepository.save(bill);
        saveBillDetail(productListToBill, bill, productQuantityMap);

        return ResponseEntity.status(HttpStatus.OK).body(Message.map(Message.CREATE_BILL_SUCCESS));
    }

    private void saveBill(Bill bill, BillRequest billRequest, Long saleId, Map<Product, Integer> productList) {
        LocalDateTime today = LocalDateTime.now();
        bill.setSellDate(today);
        bill.setNotes(billRequest.getNotes());
        bill.setOrderStatus(1);
        bill.setSaleId(saleId);
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = productList.entrySet().stream().
                map(entry -> entry.getKey().getPrdSellPrice().multiply(new BigDecimal(entry.getValue()))).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQuantity = productList.values().stream().mapToInt(integer -> integer).sum();
        bill.setTotalQuantity(totalQuantity);

        bill.setTotalPrice(totalPrice);

    }

    private void saveBillDetail(List<Product> productList, Bill bill, Map<Product, Integer> productQuantityMap) {
        for (Product product : productList) {
            DetailBill detailBill = new DetailBill();
            detailBill.setBillId(bill.getBillId());
            detailBill.setProductId(product.getProductId());
            detailBill.setQuantity(productQuantityMap.get(product));
            detailBill.setPrice(product.getPrdSellPrice()); // Need to fix
//            System.out.println(product.getPrdSellPrice().multiply(new BigDecimal(detailBill.getQuantity())));
            detailBillRepository.save(detailBill);
        }
    }

    @Override
    public ResponseEntity<?> getBillList(HttpServletRequest request, Pageable pageable) {
        Long userId = authenticateService.getUserIdByToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.USER_NOT_FOUND));
        }
        Page<Bill> billPage;
        billPage = billRepository.findAll(pageable);

        if (billPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Message.map("No bills found"));
        }

        List<DetailBill> allDetailBills = detailBillRepository.findByBillIdIn(
                        billPage.stream()
                                .toList() // Thay thế Collectors.toList()
                                .stream()
                                .map(Bill::getBillId)
                                .toList() // Thay thế Collectors.toList()
                ).stream()
                .toList();

        Map<Long, List<DetailBill>> detailBillMap = allDetailBills.stream()
                .collect(Collectors.groupingBy(DetailBill::getBillId));
        //allDetailBills co danh sach cac detail bill va dung groupingBy de map cac detail bill co dung billId voi nhau vao 1 map

        Map<Long, Product> productMap;

        productMap = productRepository.findByProductIdIn(allDetailBills.stream()
                        .map(DetailBill::getProductId)
                        .distinct()
                        .collect(Collectors.toList()))
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
    public ResponseEntity<?> getBillListForUser(HttpServletRequest request, Pageable pageable) {
        Long userId = authenticateService.getUserIdByToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map(Message.USER_NOT_FOUND));
        }
        Page<Bill> billPage;
        Specification<Bill> spec = buildSpecification(userId);
        billPage = billRepository.findAll(spec, pageable);

        if (billPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.map("No bills found"));
        }

        List<DetailBill> allDetailBills = detailBillRepository.findByBillIdIn(
                        billPage.stream()
                                .toList() // Thay thế Collectors.toList()
                                .stream()
                                .map(Bill::getBillId)
                                .toList() // Thay thế Collectors.toList()
                ).stream()
                .toList();

        Map<Long, List<DetailBill>> detailBillMap = allDetailBills.stream()
                .collect(Collectors.groupingBy(DetailBill::getBillId));
        //allDetailBills co danh sach cac detail bill va dung groupingBy de map cac detail bill co dung billId voi nhau vao 1 map

        Map<Long, Product> productMap;

        productMap = productRepository.findByProductIdIn(allDetailBills.stream()
                        .map(DetailBill::getProductId)
                        .distinct()
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));

        List<BillResponse> billResponseList = billPage.stream().map(bill -> {
            BillResponse billResponse = BillResponse.builder()
                    .billId(bill.getBillId())
                    .sellDate(bill.getSellDate().format(formatter))
                    .saleName(userRepository.getUserNameById(bill.getSaleId()) == null
                            ? ""
                            : userRepository.getUserNameById(bill.getSaleId()))
                    .notes(bill.getNotes())
                    .totalQuantity(bill.getTotalQuantity())
                    .totalPrice(bill.getTotalPrice())
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
    public ResponseEntity<?> getRevenueForAllStores() {
        List<Object[]> monthlyRevenueData = billRepository.calculateMonthlyRevenueForCurrentYear();

        // Tạo một Map để lưu doanh thu của từng tháng, mặc định là 0
        Map<Integer, Double> monthlyRevenueMap = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            monthlyRevenueMap.put(month, 0.0);
        }

        // Cập nhật doanh thu cho các tháng có dữ liệu
        for (Object[] entry : monthlyRevenueData) {
            Integer month = (Integer) entry[0];
            BigDecimal revenue = (BigDecimal) entry[1];
            monthlyRevenueMap.put(month, revenue.doubleValue()); // Chuyển BigDecimal sang Double
        }

        // Chuyển Map thành danh sách để trả về, giữ đúng định dạng
        List<Map<String, Object>> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("revenue", monthlyRevenueMap.get(month));
            result.add(monthData);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<?> getMonthlyRevenue(String dateInput) {
        LocalDate today = LocalDate.now();

        // Lấy ngày đầu tuần (Thứ Hai)
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        // Lấy ngày cuối tuần (Chủ Nhật)
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // Nếu dateInput không null hoặc rỗng, chuyển đổi nó thành LocalDate
        final LocalDate inputDate = (dateInput != null && !dateInput.isEmpty()) ? LocalDate.parse(dateInput) : null; // Đảm bảo final

        // Nếu inputDate không null, chỉ lấy doanh thu của ngày đó
        LocalDateTime startOfDay = inputDate != null ? inputDate.atStartOfDay() : startOfWeek.atStartOfDay();
        LocalDateTime endOfDay = inputDate != null ? inputDate.atTime(LocalTime.MAX) : endOfWeek.atTime(LocalTime.MAX);

        // Lấy dữ liệu doanh thu trong khoảng thời gian (tuần hoặc ngày theo dateInput)
        List<Object[]> dailyRevenueData = billRepository.calculateRevenueBetweenDates(startOfDay, endOfDay);

        // Tạo danh sách chứa doanh thu theo ngày trong khoảng thời gian
        final List<Map.Entry<LocalDate, BigDecimal>> revenues = new ArrayList<>(); // Đặt final ở đây

        // Lặp qua từng dòng dữ liệu trả về từ repository
        for (Object[] row : dailyRevenueData) {
            java.sql.Date sqlDate = (java.sql.Date) row[0];
            LocalDate date = sqlDate.toLocalDate();  // Chuyển đổi java.sql.Date sang LocalDate
            BigDecimal revenue = (BigDecimal) row[1];
            revenues.add(new AbstractMap.SimpleEntry<>(date, revenue));
        }

        // Nếu không có dữ liệu doanh thu cho ngày nào, trả về doanh thu là 0
        // Tạo một danh sách từ ngày đầu tuần đến cuối tuần hoặc ngày chỉ định và gán giá trị doanh thu là 0 cho các ngày không có dữ liệu
        for (LocalDate date = startOfWeek; date.isBefore(endOfWeek.plusDays(1)); date = date.plusDays(1)) {
            boolean found = false;
            for (Map.Entry<LocalDate, BigDecimal> entry : revenues) {
                if (entry.getKey().equals(date)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                revenues.add(new AbstractMap.SimpleEntry<>(date, BigDecimal.ZERO));  // Thêm doanh thu là 0 cho các ngày không có dữ liệu
            }
        }

        // Nếu có ngày inputDate, chỉ trả về doanh thu của ngày đó, nếu không thì trả về doanh thu của cả tuần
        if (inputDate != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    revenues.stream()
                            .filter(entry -> entry.getKey().equals(inputDate)) // Sử dụng lambda để lọc doanh thu của ngày inputDate
                            .collect(Collectors.toList())
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(revenues);
        }
    }

    private Specification<Bill> buildSpecification(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (userId != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("saleId"), userId));
            }

            return predicate;
        };
    }

}
