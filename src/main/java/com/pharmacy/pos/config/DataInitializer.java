package com.pharmacy.pos.config;

import com.pharmacy.pos.iam.entity.Permission;
import com.pharmacy.pos.iam.entity.Role;
import com.pharmacy.pos.iam.entity.RolePermission;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.repository.PermissionRepository;
import com.pharmacy.pos.iam.repository.RolePermissionRepository;
import com.pharmacy.pos.iam.repository.RoleRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Initializes default permissions, SUPERADMIN role, and root superadmin account.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.pharmacy.pos.customer.repository.DoctorRepository doctorRepository;
    private final com.pharmacy.pos.catalog.repository.CategoryRepository categoryRepository;
    private final com.pharmacy.pos.catalog.repository.ProductRepository productRepository;
    private final com.pharmacy.pos.catalog.repository.ProductUnitRepository productUnitRepository;
    private final com.pharmacy.pos.branch.repository.BranchRepository branchRepository;

    /** All permission codes used by the application */
    private static final List<String> DEFAULT_PERMISSION_CODES = List.of(
            // Organization & Multi-Tenant Governance
            "organization.view", "organization.create", "organization.update", "organization.delete", "organization.manage",
            // Branch
            "branch.view", "branch.create", "branch.update", "branch.delete", "branch.settings.view", "branch.settings.update",
            // Device
            "device.view", "device.create", "device.update", "device.delete",
            // User & Role Governance
            "user.view", "user.create", "user.update", "user.delete", "user.manage",
            "role.view", "role.create", "role.update", "role.delete", "role.manage",
            "permission.view", "permission.create", "permission.update", "permission.delete",
            // Product & Formulary
            "product.view", "product.create", "product.update", "product.delete", "product.edit_price",
            "categories.view", "categories.create", "categories.update", "categories.delete",
            "suppliers.view", "suppliers.create", "suppliers.update", "suppliers.delete",
            "active-ingredients.view", "active-ingredients.create", "active-ingredients.update", "active-ingredients.delete",
            "drug_interaction.view", "drug_interaction.create", "drug_interaction.update", "drug_interaction.delete",
            // Stock & Batches
            "inventory.view", "product_batches.view", "product_batches.create", "product_batches.update", "product_batches.delete",
            "stock_movement.view", "stock.adjust", "stock.transfer",
            // Purchase & GRN
            "purchase.view", "purchase.create", "purchase.update", "purchase.delete", "purchase.approve",
            "goods_receipt.view", "goods_receipt.create", "goods_receipt.delete",
            // Customer, Doctor, Patient & Prescriptions
            "customer.view", "customer.create", "customer.update", "customer.delete",
            "doctor.view", "doctor.create", "doctor.update", "doctor.delete", "doctor.portal.view", "doctor.schedule.manage",
            "prescription.view", "prescription.create", "prescription.update", "prescription.delete",
            "appointment.view", "appointment.create", "appointment.update", "appointment.delete",
            "telehealth.view", "telehealth.create", "telehealth.manage",
            "patient.view", "patient.create", "patient.update",
            // Orders & POS Payments
            "order.view", "order.create", "order.update", "order.delete", "order.void", "order.refund", "order.return",
            "payment.view", "payment.create", "payment.update", "payment.delete",
            "promotion.view", "promotion.create", "promotion.update", "promotion.delete",
            "loyalty.view", "loyalty.create", "loyalty.update", "loyalty.delete",
            // Reports, Notifications, Audit, Shifts
            "report.view", "notification.view", "audit.view", "audit_log.view",
            "shift.view", "shift.open", "shift.close", "shift.create", "shift.update", "shift.delete",
            "settings.manage"
    );

    @Override
    public void run(String... args) {
        // 1. Ensure SUPERADMIN role exists (The highest master role in the system)
        Role superAdminRole = roleRepository.findByName("SUPERADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("SUPERADMIN");
                    r.setSystemRole(true);
                    return roleRepository.save(r);
                });

        // 2. Ensure all default permissions exist
        int createdPermissions = 0;
        for (String code : DEFAULT_PERMISSION_CODES) {
            if (permissionRepository.findByCode(code).isEmpty()) {
                Permission permission = new Permission();
                permission.setCode(code);
                permission.setDescription("Permission authority: " + code);
                permissionRepository.save(permission);
                createdPermissions++;
            }
        }
        if (createdPermissions > 0) {
            log.info("Created {} missing default permissions", createdPermissions);
        }

        List<Permission> allPermissions = permissionRepository.findAll();

        // 3. Grant 100% full permissions to SUPERADMIN and Owner roles
        roleRepository.findByName("Owner").ifPresent(ownerRole -> {
            for (Permission p : allPermissions) {
                if (!rolePermissionRepository.existsByRoleIdAndPermissionId(ownerRole.getId(), p.getId())) {
                    RolePermission rp = new RolePermission();
                    rp.setRole(ownerRole);
                    rp.setPermission(p);
                    rolePermissionRepository.save(rp);
                }
            }
        });

        for (Permission p : allPermissions) {
            if (!rolePermissionRepository.existsByRoleIdAndPermissionId(superAdminRole.getId(), p.getId())) {
                RolePermission rp = new RolePermission();
                rp.setRole(superAdminRole);
                rp.setPermission(p);
                rolePermissionRepository.save(rp);
            }
        }

        // 3b. Ensure DOCTOR role exists and assign medical practitioner permissions
        Role doctorRole = roleRepository.findByName("DOCTOR")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("DOCTOR");
                    r.setSystemRole(true);
                    return roleRepository.save(r);
                });

        List<String> doctorPermissions = List.of(
                "doctor.view", "doctor.update", "doctor.portal.view", "doctor.schedule.manage",
                "appointment.view", "appointment.update", "appointment.create",
                "telehealth.view", "telehealth.create", "telehealth.manage",
                "prescription.view", "prescription.create", "prescription.update",
                "patient.view", "customer.view", "customer.update",
                "product.view", "categories.view", "active-ingredients.view", "drug_interaction.view"
        );

        for (String permCode : doctorPermissions) {
            permissionRepository.findByCode(permCode).ifPresent(perm -> {
                if (!rolePermissionRepository.existsByRoleIdAndPermissionId(doctorRole.getId(), perm.getId())) {
                    RolePermission rp = new RolePermission();
                    rp.setRole(doctorRole);
                    rp.setPermission(perm);
                    rolePermissionRepository.save(rp);
                }
            });
        }
        log.info("Initialized DOCTOR role with {} medical permissions", doctorPermissions.size());

        // 3c. Ensure USER (Patient / Customer) role exists and assign patient permissions
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("USER");
                    r.setSystemRole(true);
                    return roleRepository.save(r);
                });

        List<String> patientPermissions = List.of(
                "appointment.view", "appointment.create",
                "telehealth.view", "telehealth.create",
                "prescription.view", "prescription.create",
                "product.view", "categories.view", "doctor.view",
                "order.view", "order.create", "payment.view", "payment.create",
                "customer.view", "customer.update", "patient.view"
        );

        for (String permCode : patientPermissions) {
            permissionRepository.findByCode(permCode).ifPresent(perm -> {
                if (!rolePermissionRepository.existsByRoleIdAndPermissionId(userRole.getId(), perm.getId())) {
                    RolePermission rp = new RolePermission();
                    rp.setRole(userRole);
                    rp.setPermission(perm);
                    rolePermissionRepository.save(rp);
                }
            });
        }
        log.info("Initialized USER role with {} patient portal permissions", patientPermissions.size());

        // 4. Ensure master Root SuperAdmin account exists and has 123456 password
        Organization rootOrg = organizationRepository.findById(1L).orElseGet(() -> {
            Organization org = new Organization();
            org.setName("Pharmacy POS Global Platform");
            org.setSlug("platform-root");
            org.setBaseCurrency("USD");
            org.setActive(true);
            return organizationRepository.save(org);
        });

        User rootUser = userRepository.findByUsername("superadmin").orElseGet(() -> {
            User u = new User();
            u.setUsername("superadmin");
            return u;
        });

        rootUser.setPasswordHash(passwordEncoder.encode("admin123"));
        rootUser.setName("System SuperAdmin (Root)");
        rootUser.setPhone("012888999");
        rootUser.setPinCode(passwordEncoder.encode("9999"));
        rootUser.setOrganization(rootOrg);
        rootUser.setRole(superAdminRole);
        rootUser.setActive(true);
        userRepository.save(rootUser);
        log.info("Initialized root master account: superadmin / admin123 with SUPERADMIN role");

        // 4b. Seed Doctor user account (doctor1 / admin123)
        User doctorUser = userRepository.findByUsername("doctor1").orElseGet(() -> {
            User u = new User();
            u.setUsername("doctor1");
            return u;
        });
        doctorUser.setPasswordHash(passwordEncoder.encode("admin123"));
        doctorUser.setName("Dr. Heng Bunna (Specialist)");
        doctorUser.setPhone("012777888");
        doctorUser.setPinCode(passwordEncoder.encode("1234"));
        doctorUser.setOrganization(rootOrg);
        doctorUser.setRole(doctorRole);
        doctorUser.setActive(true);
        doctorUser = userRepository.save(doctorUser);
        log.info("Initialized Doctor account: doctor1 / admin123 with DOCTOR role");

        // Auto-link doctorUser to a Doctor profile
        final User finalDoctorUser = doctorUser;
        if (!doctorRepository.existsByUserId(finalDoctorUser.getId())) {
            com.pharmacy.pos.customer.entity.Doctor linkedDoctor = new com.pharmacy.pos.customer.entity.Doctor();
            linkedDoctor.setUser(finalDoctorUser);
            linkedDoctor.setName(finalDoctorUser.getName());
            linkedDoctor.setPhone(finalDoctorUser.getPhone());
            linkedDoctor.setLicenseNumber("DOC-KH-AUTO-001");
            linkedDoctor.setClinicName("សាខាកណ្តាល (Main Branch)");
            linkedDoctor.setSpecialty("General Medicine (ព្យាបាលទូទៅ)");
            linkedDoctor.setDegree("MD - Specialist");
            linkedDoctor.setExperienceYears(5);
            linkedDoctor.setRating(4.9);
            linkedDoctor.setReviewsCount(0);
            linkedDoctor.setFee(20.0);
            linkedDoctor.setAvailableSlots("09:00 AM, 10:30 AM, 02:00 PM, 04:15 PM");
            linkedDoctor.setAvailableDays("Mon, Tue, Wed, Thu, Fri");
            doctorRepository.save(linkedDoctor);
            log.info("Auto-linked doctor1 user to a Doctor Profile");
        }

        // 4c. Seed Patient user account (patient1 / 123456)
        User patientUser = userRepository.findByUsername("patient1").orElseGet(() -> {
            User u = new User();
            u.setUsername("patient1");
            return u;
        });
        patientUser.setPasswordHash(passwordEncoder.encode("123456"));
        patientUser.setName("Samet Moeun (Patient)");
        patientUser.setPhone("012345678");
        patientUser.setOrganization(rootOrg);
        patientUser.setRole(userRole);
        patientUser.setActive(true);
        userRepository.save(patientUser);
        log.info("Initialized Patient account: patient1 / 123456 with USER role");

        // 5. Seed Master Branches
        seedMasterBranches(rootOrg);

        // 6. Seed Master Categories for front-office & POS if empty
        seedMasterCategories(rootOrg);

        // 7. Seed Master Doctors for patient appointments if empty
        seedMasterDoctors();

        // 8. Seed Master Products with pricing & units if empty
        seedMasterProducts(rootOrg);
    }

    private void seedMasterBranches(Organization rootOrg) {
        if (branchRepository.count() == 0) {
            com.pharmacy.pos.branch.entity.Branch b1 = new com.pharmacy.pos.branch.entity.Branch();
            b1.setOrganization(rootOrg);
            b1.setCode("BR-HQ-01");
            b1.setName("សាខាកណ្តាល (Main Central Branch)");
            b1.setLocation("St. 271, Sangkat Boeung Tumpun, Phnom Penh");
            b1.setPhone("023 888 111");
            b1.setActive(true);
            branchRepository.save(b1);

            com.pharmacy.pos.branch.entity.Branch b2 = new com.pharmacy.pos.branch.entity.Branch();
            b2.setOrganization(rootOrg);
            b2.setCode("BR-TK-02");
            b2.setName("សាខាទួលគោក (Toul Kork Branch)");
            b2.setLocation("St. 598, Sangkat Toul Kork, Phnom Penh");
            b2.setPhone("023 888 222");
            b2.setActive(true);
            branchRepository.save(b2);

            com.pharmacy.pos.branch.entity.Branch b3 = new com.pharmacy.pos.branch.entity.Branch();
            b3.setOrganization(rootOrg);
            b3.setCode("BR-BKK-03");
            b3.setName("សាខាបឹងកេងកង (BKK Branch)");
            b3.setLocation("St. 310, Sangkat BKK1, Phnom Penh");
            b3.setPhone("023 888 333");
            b3.setActive(true);
            branchRepository.save(b3);

            log.info("Seeded 3 master branches for Root Organization");
        }
    }

    private void seedMasterCategories(Organization rootOrg) {
        if (categoryRepository.count() == 0) {
            List<String[]> catData = List.of(
                new String[]{"General Health & Wellness", "សុខភាពទូទៅ"},
                new String[]{"Pain Relief & Fever", "បំបាត់ការឈឺចាប់ & ក្តៅខ្លួន"},
                new String[]{"Antibiotics & Anti-Infectives", "ថ្នាំផ្សះ & ប្រឆាំងមេរោគ"},
                new String[]{"Vitamins & Supplements", "វីតាមីន & អាហារបំប៉ន"},
                new String[]{"Cardiology & Chronic Care", "រោគបេះដូង & ជំងឺរ៉ាំរ៉ៃ"},
                new String[]{"Pediatrics & Child Care", "រោគកុមារ & ទារក"},
                new String[]{"Gastrointestinal Care", "ក្រពះ & ពោះវៀន"},
                new String[]{"Medical Devices & First Aid", "ឧបករណ៍សុខភាព & សង្គ្រោះបឋម"}
            );

            for (String[] c : catData) {
                com.pharmacy.pos.catalog.entity.Category cat = new com.pharmacy.pos.catalog.entity.Category();
                cat.setOrganization(rootOrg);
                cat.setName(c[0]);
                cat.setNameKh(c[1]);
                cat.setActive(true);
                categoryRepository.save(cat);
            }
            log.info("Seeded master categories in database");
        }
    }

    private void seedMasterDoctors() {
        List<com.pharmacy.pos.customer.entity.Doctor> existing = doctorRepository.findAll();
        for (com.pharmacy.pos.customer.entity.Doctor d : existing) {
            boolean changed = false;
            if (d.getSpecialty() == null || d.getSpecialty().isBlank()) {
                d.setSpecialty("General Medicine (ព្យាបាលទូទៅ)");
                changed = true;
            }
            if (d.getClinicName() == null || d.getClinicName().isBlank() || d.getClinicName().contains("Health Center") || d.getClinicName().contains("Clinic")) {
                d.setClinicName("សាខាកណ្តាល (Main Branch)");
                changed = true;
            }
            if (d.getFee() == null) {
                d.setFee(20.0);
                changed = true;
            }
            if (d.getRating() == null) {
                d.setRating(4.9);
                changed = true;
            }
            if (d.getExperienceYears() == null) {
                d.setExperienceYears(8);
                changed = true;
            }
            if (d.getDegree() == null) {
                d.setDegree("MD - Medical Specialist");
                changed = true;
            }
            if (d.getAvailableSlots() == null) {
                d.setAvailableSlots("09:00 AM, 10:30 AM, 02:00 PM, 04:15 PM");
                changed = true;
            }
            if (d.getAvailableDays() == null) {
                d.setAvailableDays("Mon, Tue, Wed, Thu, Fri");
                changed = true;
            }
            if (changed) {
                doctorRepository.save(d);
            }
        }

        if (existing.stream().noneMatch(d -> d.getName().contains("Chea Sophea"))) {
            com.pharmacy.pos.customer.entity.Doctor d1 = new com.pharmacy.pos.customer.entity.Doctor();
            d1.setName("Dr. Chea Sophea (វេជ្ជបណ្ឌិត ជា សុភា)");
            d1.setLicenseNumber("DOC-KH-8821");
            d1.setPhone("+855 12 345 678");
            d1.setImageUrl("https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&q=80&w=400");
            d1.setClinicName("សាខាកណ្តាល (Main Branch)");
            d1.setSpecialty("Cardiology (រោគបេះដូង)");
            d1.setDegree("MD, PhD - Senior Cardiologist");
            d1.setExperienceYears(14);
            d1.setRating(4.9);
            d1.setReviewsCount(128);
            d1.setFee(25.0);
            d1.setAvailableSlots("09:00 AM, 10:30 AM, 02:00 PM, 04:15 PM");
            d1.setAvailableDays("Mon, Tue, Thu, Fri");
            doctorRepository.save(d1);
        }

        if (existing.stream().noneMatch(d -> d.getName().contains("Heng Bunna"))) {
            com.pharmacy.pos.customer.entity.Doctor d2 = new com.pharmacy.pos.customer.entity.Doctor();
            d2.setName("Dr. Heng Bunna (វេជ្ជបណ្ឌិត ហេង ប៊ុនណា)");
            d2.setLicenseNumber("DOC-KH-9104");
            d2.setPhone("+855 16 888 999");
            d2.setImageUrl("https://images.unsplash.com/photo-1537368910025-700350fe46c7?auto=format&fit=crop&q=80&w=400");
            d2.setClinicName("សាខាទួលគោក (Toul Kork Branch)");
            d2.setSpecialty("Pediatrics (រោគកុមារ)");
            d2.setDegree("MD - Specialist in Child Health");
            d2.setExperienceYears(10);
            d2.setRating(4.95);
            d2.setReviewsCount(210);
            d2.setFee(20.0);
            d2.setAvailableSlots("08:30 AM, 11:00 AM, 01:30 PM, 03:30 PM");
            d2.setAvailableDays("Mon, Wed, Fri, Sat");
            doctorRepository.save(d2);
        }

        if (existing.stream().noneMatch(d -> d.getName().contains("Keo Meas"))) {
            com.pharmacy.pos.customer.entity.Doctor d3 = new com.pharmacy.pos.customer.entity.Doctor();
            d3.setName("Dr. Keo Meas (វេជ្ជបណ្ឌិត កែវ មាស)");
            d3.setLicenseNumber("DOC-KH-7732");
            d3.setPhone("+855 92 111 222");
            d3.setImageUrl("https://images.unsplash.com/photo-1559839734-2b71ea197ec2?auto=format&fit=crop&q=80&w=400");
            d3.setClinicName("សាខាបឹងកេងកង (BKK Branch)");
            d3.setSpecialty("General Practice (ព្យាបាលទូទៅ)");
            d3.setDegree("MD - Family Medicine");
            d3.setExperienceYears(12);
            d3.setRating(4.85);
            d3.setReviewsCount(94);
            d3.setFee(15.0);
            d3.setAvailableSlots("09:30 AM, 11:30 AM, 02:30 PM, 05:00 PM");
            d3.setAvailableDays("Everyday");
            doctorRepository.save(d3);
        }

        if (existing.stream().noneMatch(d -> d.getName().contains("Vannak Chan"))) {
            com.pharmacy.pos.customer.entity.Doctor d4 = new com.pharmacy.pos.customer.entity.Doctor();
            d4.setName("Pharm. Vannak Chan (ឱសថការី វណ្ណៈ ចាន់)");
            d4.setLicenseNumber("PHARM-KH-4412");
            d4.setPhone("+855 77 333 444");
            d4.setImageUrl("https://images.unsplash.com/photo-1594824813566-788530791f4d?auto=format&fit=crop&q=80&w=400");
            d4.setClinicName("សាខាពិគ្រោះអនឡាញ (Tele-Consult Branch)");
            d4.setSpecialty("Clinical Pharmacy (ពិគ្រោះយោបល់ប្រើប្រាស់ថ្នាំ)");
            d4.setDegree("PharmD - Senior Clinical Pharmacist");
            d4.setExperienceYears(8);
            d4.setRating(5.0);
            d4.setReviewsCount(176);
            d4.setFee(10.0);
            d4.setAvailableSlots("08:00 AM, 10:00 AM, 01:00 PM, 04:00 PM, 06:30 PM");
            d4.setAvailableDays("Mon, Tue, Wed, Thu, Fri, Sat");
            doctorRepository.save(d4);
        }
    }

    private void seedMasterProducts(Organization rootOrg) {
        if (productRepository.count() == 0) {
            List<com.pharmacy.pos.catalog.entity.Category> categories = categoryRepository.findAll();
            com.pharmacy.pos.catalog.entity.Category defaultCat = categories.isEmpty() ? null : categories.get(0);
            com.pharmacy.pos.catalog.entity.Category painCat = categories.stream().filter(c -> c.getName().contains("Pain")).findFirst().orElse(defaultCat);
            com.pharmacy.pos.catalog.entity.Category vitCat = categories.stream().filter(c -> c.getName().contains("Vitamins")).findFirst().orElse(defaultCat);
            com.pharmacy.pos.catalog.entity.Category antiCat = categories.stream().filter(c -> c.getName().contains("Antibiotics")).findFirst().orElse(defaultCat);
            com.pharmacy.pos.catalog.entity.Category gastroCat = categories.stream().filter(c -> c.getName().contains("Gastro")).findFirst().orElse(defaultCat);
            com.pharmacy.pos.catalog.entity.Category devCat = categories.stream().filter(c -> c.getName().contains("Device")).findFirst().orElse(defaultCat);

            createProductWithUnit(rootOrg, "MED-PARA-500", "Paracetamol 500mg (ប៉ារ៉ាសេតាម៉ុល)", painCat, "2.50", "Box / 10 Strips", "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?auto=format&fit=crop&q=80&w=400", false);
            createProductWithUnit(rootOrg, "MED-VITC-1000", "Vitamin C 1000mg Effervescent (វីតាមីន C)", vitCat, "8.00", "Tube / 20 Tabs", "https://images.unsplash.com/photo-1550572017-edd951aa8f72?auto=format&fit=crop&q=80&w=400", false);
            createProductWithUnit(rootOrg, "MED-AMOX-500", "Amoxicillin 500mg (អាម៉ុកស៊ីស៊ីលីន)", antiCat, "6.50", "Box / 20 Capsules", "https://images.unsplash.com/photo-1471864190281-a93a3070b6de?auto=format&fit=crop&q=80&w=400", true);
            createProductWithUnit(rootOrg, "MED-OMEGA-1000", "Omega-3 Fish Oil 1000mg (ប្រេងត្រី)", vitCat, "15.00", "Bottle / 100 Softgels", "https://images.unsplash.com/photo-1577401239170-897942555fb3?auto=format&fit=crop&q=80&w=400", false);
            createProductWithUnit(rootOrg, "MED-OMEP-20", "Omeprazole 20mg (ថ្នាំក្រពះ)", gastroCat, "5.50", "Box / 14 Capsules", "https://images.unsplash.com/photo-1585435557343-3b092031a831?auto=format&fit=crop&q=80&w=400", false);
            createProductWithUnit(rootOrg, "DEV-BPMON-01", "Digital Blood Pressure Monitor (ម៉ាស៊ីនវាស់សម្ពាធឈាម)", devCat, "35.00", "Unit / Set", "https://images.unsplash.com/photo-1631815589968-fdb09a223b1e?auto=format&fit=crop&q=80&w=400", false);

            log.info("Seeded master products with units and prices in database");
        }
    }

    private void createProductWithUnit(Organization org, String sku, String brandName, com.pharmacy.pos.catalog.entity.Category cat, String priceStr, String unitName, String imgUrl, boolean rx) {
        com.pharmacy.pos.catalog.entity.Product product = new com.pharmacy.pos.catalog.entity.Product();
        product.setOrganization(org);
        product.setSku(sku);
        product.setBrandName(brandName);
        product.setCategory(cat);
        product.setImageUrl(imgUrl);
        product.setRequiresPrescription(rx);
        product.setActive(true);
        product = productRepository.save(product);

        com.pharmacy.pos.catalog.entity.ProductUnit unit = new com.pharmacy.pos.catalog.entity.ProductUnit();
        unit.setProduct(product);
        unit.setUnitName(unitName);
        unit.setBarcode(sku + "-BC");
        unit.setConversionFactor(1);
        unit.setBaseUnit(true);
        unit.setSellingPrice(new java.math.BigDecimal(priceStr));
        unit.setCostPrice(new java.math.BigDecimal(priceStr).multiply(new java.math.BigDecimal("0.70")));
        productUnitRepository.save(unit);
    }
}