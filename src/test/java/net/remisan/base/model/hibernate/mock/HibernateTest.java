package net.remisan.base.model.hibernate.mock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.remisan.base.model.TestEntity;

@Entity
@Table(name = "test")
public class HibernateTest implements TestEntity {

    private static final long serialVersionUID = -9110828842620229686L;

    @Id
    @Column(name = "test_id")
    private Long id;
    
    @Column(name = "test_name", length = 20)
    private String name;
    
    @Column(name = "test_dummy", length = 20)
    private String dummy;
    
    @Column(name = "test_foo", length = 20)
    private String foo;
    
    @Column(name = "test_bar", length = 20)
    private String bar;
    
    public HibernateTest() {
        super();
    }
    
    public HibernateTest(Long id) {
        super();
        this.id = id;
    }
    
    public HibernateTest(String name) {
        super();
        this.name = name;
    }
    
    public HibernateTest(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the dummy
     */
    public String getDummy() {
        return this.dummy;
    }

    /**
     * @return the foo
     */
    public String getFoo() {
        return this.foo;
    }

    /**
     * @return the bar
     */
    public String getBar() {
        return this.bar;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param dummy the dummy to set
     */
    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    /**
     * @param foo the foo to set
     */
    public void setFoo(String foo) {
        this.foo = foo;
    }

    /**
     * @param bar the bar to set
     */
    public void setBar(String bar) {
        this.bar = bar;
    }

    public boolean isNew() {
        return this.id == null;
    }

    /**
     * @param anObject
     * @return
     * @see java.lang.String#equals(java.lang.Object)
     */
    public boolean equals(Object anObject) {
        if (!(anObject instanceof TestEntity)) {
            return false;
        }
        
        TestEntity entity = (TestEntity) anObject;
        return  ((this.id == null && entity.getId() == null) || this.id.equals(entity.getId()))
                && ((this.name == null && entity.getName() == null) || this.name.equals(entity.getName()))
                && ((this.dummy == null && entity.getDummy() == null) || this.dummy.equals(entity.getDummy()))
                && ((this.foo == null && entity.getFoo() == null) || this.foo.equals(entity.getFoo()))
                && ((this.bar == null && entity.getBar() == null) || this.bar.equals(entity.getBar()));
    }
}
