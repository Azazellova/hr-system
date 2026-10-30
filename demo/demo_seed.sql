-- Demo data
BEGIN;
INSERT INTO employee (employee_id, last_name, first_name, middle_name, birth_date,
                      phone, email, department_id, position_id, employment_date, employment_status,
                      work_schedule, is_active) VALUES
    (301,'Smirnova','Anna','Igorevna','1991-04-12','+79990000001','anna.smirnova@vector.example',101,201,'2024-03-01','ACTIVE','5/2',true),
    (302,'Orlov','Maxim','Pavlovich','1996-08-22','+79990000002','maxim.orlov@vector.example',102,203,'2025-01-15','ACTIVE','5/2',true),
    (303,'Kuznetsova','Elena','Andreevna','1993-02-07','+79990000003','elena.kuznetsova@vector.example',112,212,'2024-09-10','ACTIVE','5/2',true)
ON CONFLICT (employee_id) DO NOTHING;

INSERT INTO departments (department_id, name, parent_department_id, manager_id) VALUES
    (101,'Engineering',NULL,NULL),
    (102,'Backend Development',101,NULL),
    (103,'Frontend Development',101,NULL),
    (104,'Mobile Development',101,NULL),
    (105,'Infrastructure and Operations',NULL,NULL),
    (106,'DevOps',105,NULL),
    (107,'Quality Assurance',NULL,NULL),
    (108,'Test Automation',107,NULL),
    (109,'Product and Analytics',NULL,NULL),
    (110,'Business Analysis',109,NULL),
    (111,'People and Administration',NULL,NULL),
    (112,'Recruitment',111,NULL),
    (113,'Customer Support',NULL,NULL)
ON CONFLICT (department_id) DO NOTHING;

INSERT INTO positions (position_id, name, department_id) VALUES
     (201,'Head of Engineering',101),
     (202,'Senior Java Developer',102),
     (203,'Java Developer',102),
     (204,'Frontend Developer',103),
     (205,'Mobile Developer',104),
     (206,'DevOps Engineer',106),
     (207,'System Administrator',105),
     (208,'QA Automation Engineer',108),
     (209,'QA Engineer',107),
     (210,'Product Manager',109),
     (211,'Business Analyst',110),
     (212,'HR Specialist',112),
     (213,'Head of HR',111),
     (214,'Support Specialist',113),
     (215,'Tech Lead',102)
ON CONFLICT (position_id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('departments','department_id'),
              (SELECT MAX(department_id) FROM departments), true);
SELECT setval(pg_get_serial_sequence('positions','position_id'),
              (SELECT MAX(position_id) FROM positions), true);
SELECT setval(pg_get_serial_sequence('employee','employee_id'),
              (SELECT MAX(employee_id) FROM employee), true);
COMMIT;
