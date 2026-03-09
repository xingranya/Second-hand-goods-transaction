package com.secondhand.config;

import com.secondhand.entity.Message;
import com.secondhand.entity.Product;
import com.secondhand.entity.Review;
import com.secondhand.entity.Transaction;
import com.secondhand.entity.User;
import com.secondhand.entity.WantedPost;
import com.secondhand.repository.MessageRepository;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.ReviewRepository;
import com.secondhand.repository.TransactionRepository;
import com.secondhand.repository.UserRepository;
import com.secondhand.repository.WantedPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DemoDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WantedPostRepository wantedPostRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, User> users = seedUsers();
        Map<String, Product> products = seedProducts(users);
        seedWantedPosts(users);
        seedTransactions(users, products);
        seedMessages(users, products);
        seedReviews(users, products);
    }

    private Map<String, User> seedUsers() {
        List<DemoUserSeed> userSeeds = Arrays.asList(
                new DemoUserSeed("alice", "123456", "alice@campus.com", "李晨", "13800000001", "主校区", "20220001", true),
                new DemoUserSeed("bob", "123456", "bob@campus.com", "王雨", "13800000002", "主校区", "20220002", true),
                new DemoUserSeed("charlie", "123456", "charlie@campus.com", "张宁", "13800000003", "东校区", "20220003", true),
                new DemoUserSeed("diana", "123456", "diana@campus.com", "陈语", "13800000004", "西校区", "20220004", true),
                new DemoUserSeed("eric", "123456", "eric@campus.com", "刘哲", "13800000005", "主校区", "20220005", false),
                new DemoUserSeed("fiona", "123456", "fiona@campus.com", "周晴", "13800000006", "东校区", "20220006", true),
                new DemoUserSeed("george", "123456", "george@campus.com", "赵凯", "13800000007", "西校区", "20220007", false),
                new DemoUserSeed("helen", "123456", "helen@campus.com", "孙悦", "13800000008", "主校区", "20220008", true),
                new DemoUserSeed("ivan", "123456", "ivan@campus.com", "吴凡", "13800000009", "东校区", "20220009", true),
                new DemoUserSeed("jenny", "123456", "jenny@campus.com", "郑雪", "13800000010", "西校区", "20220010", true)
        );

        Map<String, User> userMap = userRepository.findAll()
                .stream()
                .collect(Collectors.toMap(User::getUsername, item -> item, (a, b) -> a));

        for (DemoUserSeed seed : userSeeds) {
            if (userMap.containsKey(seed.username)) {
                continue;
            }
            User user = new User();
            user.setUsername(seed.username);
            user.setPassword(passwordEncoder.encode(seed.password));
            user.setEmail(seed.email);
            user.setName(seed.name);
            user.setPhoneNumber(seed.phoneNumber);
            user.setSchool(seed.school);
            user.setStudentNo(seed.studentNo);
            user.setVerified(seed.verified);
            User saved = userRepository.save(user);
            userMap.put(saved.getUsername(), saved);
        }

        return userMap;
    }

    private Map<String, Product> seedProducts(Map<String, User> users) {
        List<DemoProductSeed> seeds = Arrays.asList(
                new DemoProductSeed("p-macbook", "MacBook Air M1 8+256", "轻度办公，续航正常，附赠电脑包", "4899", "数码", "95新", "AVAILABLE", "https://images.unsplash.com/photo-1517336714739-489689fd1ca8?w=1200", "alice"),
                new DemoProductSeed("p-ipad", "iPad 9 64G 国行", "仅课堂记笔记使用，机身无磕碰", "1899", "数码", "9成新", "AVAILABLE", "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=1200", "alice"),
                new DemoProductSeed("p-bike", "捷安特山地车", "可正常骑行，刹车灵敏", "680", "出行", "8成新", "AVAILABLE", "https://images.unsplash.com/photo-1507035895480-2b3156c31fc8?w=1200", "bob"),
                new DemoProductSeed("p-keyboard", "罗技机械键盘", "茶轴，灯效正常，带原盒", "260", "数码", "9成新", "AVAILABLE", "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=1200", "bob"),
                new DemoProductSeed("p-calculus", "高等数学教材上下册", "笔记较全，适合期末复习", "45", "书籍", "8成新", "AVAILABLE", "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=1200", "charlie"),
                new DemoProductSeed("p-java", "Java 核心技术卷 I", "无缺页，少量划线", "38", "书籍", "9成新", "AVAILABLE", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=1200", "charlie"),
                new DemoProductSeed("p-ricecooker", "小熊电饭煲 2L", "宿舍可用，功能正常", "88", "生活电器", "8成新", "AVAILABLE", "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=1200", "diana"),
                new DemoProductSeed("p-lamp", "护眼台灯", "三档亮度，支持充电", "39", "生活用品", "9成新", "AVAILABLE", "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=1200", "diana"),
                new DemoProductSeed("p-headset", "索尼头戴式耳机", "音质正常，耳罩轻微磨损", "420", "数码", "8成新", "AVAILABLE", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=1200", "eric"),
                new DemoProductSeed("p-monitor", "27寸显示器 2K", "毕业转让，支持高刷", "760", "数码", "9成新", "AVAILABLE", "https://images.unsplash.com/photo-1527443224154-c4e5c1a6f4e9?w=1200", "fiona"),
                new DemoProductSeed("p-badminton", "羽毛球拍 2支装", "含拍包，适合新手", "120", "运动", "9成新", "AVAILABLE", "https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?w=1200", "george"),
                new DemoProductSeed("p-ukulele", "21寸尤克里里", "音准正常，赠调音器", "150", "乐器", "8成新", "AVAILABLE", "https://images.unsplash.com/photo-1525201548942-d8732f6617a0?w=1200", "helen"),
                new DemoProductSeed("p-printer", "惠普打印机", "可打印扫描，墨盒余量充足", "310", "数码", "8成新", "AVAILABLE", "https://images.unsplash.com/photo-1563986768609-322da13575f3?w=1200", "ivan"),
                new DemoProductSeed("p-sneaker", "耐克运动鞋 42码", "尺码不合适，几乎全新", "220", "服饰", "95新", "AVAILABLE", "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=1200", "jenny"),
                new DemoProductSeed("p-chair", "人体工学椅", "靠背可调，久坐舒服", "260", "家具", "8成新", "AVAILABLE", "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=1200", "alice"),
                new DemoProductSeed("p-router", "小米千兆路由器", "信号稳定，配件齐全", "120", "数码", "9成新", "AVAILABLE", "https://images.unsplash.com/photo-1647427060118-4911c9821b82?w=1200", "bob")
        );

        Map<String, Product> existingByKey = new HashMap<>();
        for (Product item : productRepository.findAll()) {
            if (item.getSeller() == null || item.getName() == null) {
                continue;
            }
            existingByKey.put(productUniqueKey(item.getSeller().getId(), item.getName()), item);
        }

        Map<String, Product> productMap = new HashMap<>();
        for (DemoProductSeed seed : seeds) {
            User seller = users.get(seed.sellerUsername);
            if (seller == null) {
                continue;
            }
            String key = productUniqueKey(seller.getId(), seed.name);
            Product product = existingByKey.get(key);
            if (product == null) {
                product = new Product();
                product.setName(seed.name);
                product.setDescription(seed.description);
                product.setPrice(new BigDecimal(seed.price));
                product.setCategory(seed.category);
                product.setCondition(seed.condition);
                product.setStatus(seed.status);
                product.setImageUrl(seed.imageUrl);
                product.setSeller(seller);
                product = productRepository.save(product);
                existingByKey.put(key, product);
            }
            productMap.put(seed.key, product);
        }
        return productMap;
    }

    private void seedWantedPosts(Map<String, User> users) {
        List<DemoWantedSeed> seeds = Arrays.asList(
                new DemoWantedSeed("求购 iPhone 13/14", "2200", "想要 128G，无锁无维修", "主校区", "fiona", 10),
                new DemoWantedSeed("求购 Python 数据分析教材", "50", "最好有配套习题答案", "东校区", "george", 7),
                new DemoWantedSeed("求购 24 寸显示器", "500", "接口支持 HDMI 即可", "西校区", "helen", 14),
                new DemoWantedSeed("求购 吉他入门款", "300", "木吉他优先，预算可小刀", "主校区", "ivan", 9),
                new DemoWantedSeed("求购 宿舍收纳柜", "120", "尺寸适配床下空间", "主校区", "jenny", 12),
                new DemoWantedSeed("求购 羽毛球鞋 41 码", "180", "李宁或尤尼克斯均可", "东校区", "eric", 8),
                new DemoWantedSeed("求购 计算机组成原理教材", "35", "版本不限，能看清即可", "西校区", "diana", 11),
                new DemoWantedSeed("求购 电动车头盔", "60", "优先全盔，颜色不限", "主校区", "charlie", 6)
        );

        Set<String> existingKeys = wantedPostRepository.findAll()
                .stream()
                .filter(item -> item.getPublisher() != null && item.getTitle() != null)
                .map(item -> item.getPublisher().getId() + "|" + item.getTitle())
                .collect(Collectors.toSet());

        for (DemoWantedSeed seed : seeds) {
            User publisher = users.get(seed.publisherUsername);
            if (publisher == null) {
                continue;
            }
            String key = publisher.getId() + "|" + seed.title;
            if (existingKeys.contains(key)) {
                continue;
            }
            WantedPost post = new WantedPost();
            post.setTitle(seed.title);
            post.setExpectedPrice(new BigDecimal(seed.expectedPrice));
            post.setDescription(seed.description);
            post.setCampus(seed.campus);
            post.setDeadline(LocalDate.now().plusDays(seed.deadlineDays));
            post.setPublisher(publisher);
            wantedPostRepository.save(post);
            existingKeys.add(key);
        }
    }

    private void seedTransactions(Map<String, User> users, Map<String, Product> products) {
        List<DemoTransactionSeed> seeds = Arrays.asList(
                new DemoTransactionSeed("seed-order-001", "p-macbook", "bob", "PENDING"),
                new DemoTransactionSeed("seed-order-002", "p-ipad", "charlie", "PAID"),
                new DemoTransactionSeed("seed-order-003", "p-bike", "diana", "SHIPPED"),
                new DemoTransactionSeed("seed-order-004", "p-keyboard", "eric", "RECEIVED"),
                new DemoTransactionSeed("seed-order-005", "p-calculus", "fiona", "COMPLETED"),
                new DemoTransactionSeed("seed-order-006", "p-java", "george", "CANCELLED"),
                new DemoTransactionSeed("seed-order-007", "p-ricecooker", "helen", "PAID"),
                new DemoTransactionSeed("seed-order-008", "p-headset", "ivan", "SHIPPED"),
                new DemoTransactionSeed("seed-order-009", "p-monitor", "jenny", "RECEIVED"),
                new DemoTransactionSeed("seed-order-010", "p-sneaker", "alice", "COMPLETED")
        );

        Set<String> existingNotes = transactionRepository.findAll()
                .stream()
                .map(Transaction::getNote)
                .filter(item -> item != null && item.startsWith("seed-order-"))
                .collect(Collectors.toSet());

        for (DemoTransactionSeed seed : seeds) {
            if (existingNotes.contains(seed.note)) {
                continue;
            }
            Product product = products.get(seed.productKey);
            User buyer = users.get(seed.buyerUsername);
            if (product == null || buyer == null || product.getSeller() == null) {
                continue;
            }
            if (buyer.getId().equals(product.getSeller().getId())) {
                continue;
            }

            Transaction transaction = new Transaction();
            transaction.setProduct(product);
            transaction.setBuyer(buyer);
            transaction.setSeller(product.getSeller());
            transaction.setAmount(product.getPrice());
            transaction.setNote(seed.note);
            Transaction saved = transactionRepository.save(transaction);

            if (!"PENDING".equals(seed.status)) {
                saved.setStatus(seed.status);
                if ("COMPLETED".equals(seed.status)) {
                    saved.setCompletedAt(LocalDateTime.now().minusDays(1));
                }
                if ("CANCELLED".equals(seed.status)) {
                    saved.setCancelledAt(LocalDateTime.now().minusHours(12));
                }
                transactionRepository.save(saved);
            }
            existingNotes.add(seed.note);
        }
    }

    private void seedMessages(Map<String, User> users, Map<String, Product> products) {
        List<DemoMessageSeed> seeds = Arrays.asList(
                new DemoMessageSeed("alice", "bob", "p-macbook", "这台 MacBook 电池循环次数多少？"),
                new DemoMessageSeed("bob", "alice", "p-macbook", "目前 186 次，续航正常。"),
                new DemoMessageSeed("charlie", "alice", "p-ipad", "可以当面验机后交易吗？"),
                new DemoMessageSeed("alice", "charlie", "p-ipad", "可以，今晚图书馆门口可以。"),
                new DemoMessageSeed("diana", "bob", "p-bike", "车架尺寸是多少？"),
                new DemoMessageSeed("bob", "diana", "p-bike", "26 寸，身高 165-180 都能骑。"),
                new DemoMessageSeed("eric", "diana", null, "求购帖看到了，台灯还在吗？"),
                new DemoMessageSeed("diana", "eric", null, "还在，可以今晚自提。")
        );

        Set<String> existingKeys = new HashSet<>();
        for (Message item : messageRepository.findAll()) {
            existingKeys.add(messageUniqueKey(
                    item.getSender() == null ? null : item.getSender().getId(),
                    item.getReceiver() == null ? null : item.getReceiver().getId(),
                    item.getProduct() == null ? null : item.getProduct().getId(),
                    item.getContent()
            ));
        }

        for (DemoMessageSeed seed : seeds) {
            User sender = users.get(seed.senderUsername);
            User receiver = users.get(seed.receiverUsername);
            Product product = seed.productKey == null ? null : products.get(seed.productKey);
            if (sender == null || receiver == null) {
                continue;
            }
            String key = messageUniqueKey(sender.getId(), receiver.getId(), product == null ? null : product.getId(), seed.content);
            if (existingKeys.contains(key)) {
                continue;
            }
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setProduct(product);
            message.setContent(seed.content);
            message.setRead(false);
            messageRepository.save(message);
            existingKeys.add(key);
        }
    }

    private void seedReviews(Map<String, User> users, Map<String, Product> products) {
        List<DemoReviewSeed> seeds = Arrays.asList(
                new DemoReviewSeed("bob", "p-macbook", 5, "成色和描述一致，沟通顺畅。"),
                new DemoReviewSeed("fiona", "p-calculus", 4, "书有些划线，但不影响使用。"),
                new DemoReviewSeed("alice", "p-sneaker", 5, "鞋子很新，发货也快。"),
                new DemoReviewSeed("diana", "p-bike", 4, "车况不错，交易很顺利。")
        );

        for (DemoReviewSeed seed : seeds) {
            User reviewer = users.get(seed.reviewerUsername);
            Product product = products.get(seed.productKey);
            if (reviewer == null || product == null) {
                continue;
            }
            if (reviewRepository.existsByProductIdAndReviewerId(product.getId(), reviewer.getId())) {
                continue;
            }
            Review review = new Review();
            review.setProduct(product);
            review.setReviewer(reviewer);
            review.setRating(seed.rating);
            review.setComment(seed.comment);
            reviewRepository.save(review);
        }
    }

    private String productUniqueKey(Long sellerId, String name) {
        return sellerId + "|" + name;
    }

    private String messageUniqueKey(Long senderId, Long receiverId, Long productId, String content) {
        String product = productId == null ? "0" : String.valueOf(productId);
        String text = content == null ? "" : content;
        return senderId + "|" + receiverId + "|" + product + "|" + text;
    }

    private static class DemoUserSeed {
        private final String username;
        private final String password;
        private final String email;
        private final String name;
        private final String phoneNumber;
        private final String school;
        private final String studentNo;
        private final boolean verified;

        private DemoUserSeed(String username, String password, String email, String name, String phoneNumber, String school, String studentNo, boolean verified) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.school = school;
            this.studentNo = studentNo;
            this.verified = verified;
        }
    }

    private static class DemoProductSeed {
        private final String key;
        private final String name;
        private final String description;
        private final String price;
        private final String category;
        private final String condition;
        private final String status;
        private final String imageUrl;
        private final String sellerUsername;

        private DemoProductSeed(String key, String name, String description, String price, String category, String condition, String status, String imageUrl, String sellerUsername) {
            this.key = key;
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.condition = condition;
            this.status = status;
            this.imageUrl = imageUrl;
            this.sellerUsername = sellerUsername;
        }
    }

    private static class DemoWantedSeed {
        private final String title;
        private final String expectedPrice;
        private final String description;
        private final String campus;
        private final String publisherUsername;
        private final int deadlineDays;

        private DemoWantedSeed(String title, String expectedPrice, String description, String campus, String publisherUsername, int deadlineDays) {
            this.title = title;
            this.expectedPrice = expectedPrice;
            this.description = description;
            this.campus = campus;
            this.publisherUsername = publisherUsername;
            this.deadlineDays = deadlineDays;
        }
    }

    private static class DemoTransactionSeed {
        private final String note;
        private final String productKey;
        private final String buyerUsername;
        private final String status;

        private DemoTransactionSeed(String note, String productKey, String buyerUsername, String status) {
            this.note = note;
            this.productKey = productKey;
            this.buyerUsername = buyerUsername;
            this.status = status;
        }
    }

    private static class DemoMessageSeed {
        private final String senderUsername;
        private final String receiverUsername;
        private final String productKey;
        private final String content;

        private DemoMessageSeed(String senderUsername, String receiverUsername, String productKey, String content) {
            this.senderUsername = senderUsername;
            this.receiverUsername = receiverUsername;
            this.productKey = productKey;
            this.content = content;
        }
    }

    private static class DemoReviewSeed {
        private final String reviewerUsername;
        private final String productKey;
        private final int rating;
        private final String comment;

        private DemoReviewSeed(String reviewerUsername, String productKey, int rating, String comment) {
            this.reviewerUsername = reviewerUsername;
            this.productKey = productKey;
            this.rating = rating;
            this.comment = comment;
        }
    }
}
