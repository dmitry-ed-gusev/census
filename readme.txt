---------------------------------------------------------------------------------------------------
  [Tech notes]
---------------------------------------------------------------------------------------------------
1. Hibernate many-to-many mapping examples:
    * Hibernate – Many-to-Many example – join table + extra column (Annotation)
       http://www.mkyong.com/hibernate/hibernate-many-to-many-example-join-table-extra-column-annotation/
    * Hibernate – Many-to-Many example (Annotation)
       http://www.mkyong.com/hibernate/hibernate-many-to-many-relationship-example-annotation/
    * Hibernate – Many-to-Many example (XML Mapping)
       http://www.mkyong.com/hibernate/hibernate-many-to-many-relationship-example/
2. Hibernate one-to-many mapping examples:
    * http://www.mkyong.com/hibernate/hibernate-one-to-many-relationship-example-annotation/

---------------------------------------------------------------------------------------------------
  [Personnel] module. Description/specials.
---------------------------------------------------------------------------------------------------
1. Department stores chief id from personnel list. If this id is empty (id <= 0), department
   chief is department's employee with max weighted position.
2. One department may have only ONE chief (count = 1 or 0), but one employee can be chief of
   many departments (count > 0).
3. One employee may work (be assigned) in many departments (count > 0), and one department may
   consist of many employees.
4. Relation between Employees and Positions is many-to-many (one employee may have more than one
   positions, and vice versa - many employees may have one position.

---------------------------------------------------------------------------------------------------
  [] module. Description/specials.
---------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------
  [] module. Description/specials.
---------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------
  [] module. Description/specials.
---------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------
  [] module. Description/specials.
---------------------------------------------------------------------------------------------------
