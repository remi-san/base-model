Spring4 Base Model [![Build Status](https://travis-ci.org/remi-san/base-model.svg?branch=master)](https://travis-ci.org/remi-san/base-model)
==================
Content
-------
This lib includes a set of classes defining :

 - **Model**:
     - [`PersistableEntity`][1]: `Interface` every persistable class should extend - `Primary Key` is a `Long` variable called `id`
     - [`User`][2]: `Interface` every user class should extend
 - **Validation**:
     - [`ValidationManager`][3]: `Validator` used to manage multiple validators
 - **Repository**:
     - [`Repository`][4]: `Interface` for every JPA2 repository of a Spring4 application - can only manage `PersistableEntity` objects
     - [`BaseJpaRepository`][5]: `Repository` implementation extending `SimpleJpaRepository` (not very convenient for injection)
     - [`HibernateRepository`][6]: `Repository` implementation for `Hibernate` repositories
 - **Manager**
     - [`Manager`][7]: `Interface` for every manager of a Spring4 application
     - [`AbstractManager`][8]: `Manager` implementation using a `Repository`
 - **Service**
     - [`Service`][9]: `Interface` for every service of a Spring4 application
     - [`AbstractService`][10]: `Service` implementation using a `Manager`

Usage
-----
Can be used in any Spring4 project using JPA2.

**Example:**
Here's the minimal example. Interfaces won't be written here but must expose every public method and must be used no matter what, that's the all point of this lib.

----------

*FooImpl.java*

    @Entity
    public class FooImpl implements Foo {
        private Long id;
        private String bar;
        
        public Long getId() {
            return this.id;
        }
        public void setId(Long) {
            this.id = id;
        }
        
        public Long getBar() {
            return this.bar;
        }
        public void setBar(Long) {
            this.bar = bar;
        }
    }
    
----------  

*FooRepositoryImpl.java*

    @Repository
    public class FooRepositoryImpl
        extends HibernateRepository<Foo>
        implements FooRepository {
        
        public FooRepository() {
            super(Foo.class);
        }
    }
    
----------  

*FooManagerImpl.java*

    @Component
    public class FooManagerImpl
        extends AbstractManager<Foo>
        implements FooManager {
    
        @Autowired
        private FooRepository repository; // Must be an interface
        
        public FooManagerImpl() {
            super();
        }
        
        @Override
        protected Repository<Foo> getRepository() {
            return this.repository;
        }
    }
    
----------  

*FooServiceImpl.java*

    @Service
    public class FooServiceImpl
        extends AbstractService<Foo>
        implements FooService {
    
        @Autowired
        private FooManager manager;
    
        @Override
        protected Manager<Foo> getManager() {
            return this.manager;
        }
    }
    
----------  

Have fun!

  [1]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/model/PersistableEntity.java
  [2]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/model/User.java
  [3]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/model/validation/ValidationManager.java
  [4]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/repository/Repository.java
  [5]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/repository/BaseJpaRepository.java
  [6]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/repository/HibernateRepository.java
  [7]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/manager/Manager.java
  [8]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/manager/AbstractManager.java
  [9]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/service/Service.java
  [10]: https://github.com/remi-san/base-model/blob/master/src/main/java/net/remisan/base/service/AbstractService.java