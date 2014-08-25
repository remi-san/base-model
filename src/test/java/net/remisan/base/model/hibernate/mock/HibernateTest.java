package net.remisan.base.model.hibernate.mock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.remisan.base.model.TestEntity;

@Entity
@Table(name = "test", schema = "public")
public class HibernateTest implements TestEntity {

    private static final long serialVersionUID = -9110828842620229686L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    public HibernateTest(String name) {
        super();
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

}
