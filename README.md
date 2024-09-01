A Java-Based Student Marks System that manages the term to term students' subjects registrations and marks:
1.  Main Program Driver File: MainForm (Swing-based)
       It has menu options and icons to the various data capture forms in the system It also has an icon for the automated term registration.
2.  Database: is cloud-based on Amazon (MySQL - Free-Tier).
     Tables: These are main tables and there are more helper and temporary tables for the system
             registration -------- subject
                  |    |              |
                  |    subject teacher
              student          |
                  |            |
                grade       teacher
                  |            |
                  |-------grade teacher
    
4.  ORM: Hibernate - uses Hibernate entities for Object-Relational Mapping and Repositories with a SessionFactory to populate the entities/database with session data. Uses the hibernate.cfg.xml for database connectivity properties.....
5.  Program has a lot of operational test data for you to explore how it works...
6.  See the folders and their contents for Program file structure.
