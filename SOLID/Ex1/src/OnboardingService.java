import java.util.*;

public class OnboardingService {

    private final FakeDb db;
    private final StudentParser parser = new StudentParser();
    private final StudentValidator validator = new StudentValidator();

    public OnboardingService(FakeDb db) {
        this.db = db;
    }

    public void registerFromRawInput(String raw) {

        Map<String, String> kv = parser.parse(raw);

        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");

        List<String> errors =
                validator.validate(name, email, phone, program);

        if (!errors.isEmpty()) {
            System.out.println("ERROR: cannot register");
            errors.forEach(e -> System.out.println("- " + e));
            return;
        }

        String id = IdUtil.nextStudentId(db.count());

        StudentRecord rec =
                new StudentRecord(id, name, email, phone, program);

        db.save(rec);

        System.out.println("OK: created student " + id);
        System.out.println("Saved. Total students: " + db.count());
        System.out.println(rec);
    }
}