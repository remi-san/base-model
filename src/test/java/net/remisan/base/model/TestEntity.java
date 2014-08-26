package net.remisan.base.model;

public interface TestEntity extends PersistableEntity {

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the dummy
     */
    String getDummy();

    /**
     * @return the foo
     */
    String getFoo();

    /**
     * @return the bar
     */
    String getBar();

    /**
     * @param name the name to set
     */
    void setName(String name);

    /**
     * @param dummy the dummy to set
     */
    void setDummy(String dummy);

    /**
     * @param foo the foo to set
     */
    void setFoo(String foo);

    /**
     * @param bar the bar to set
     */
    void setBar(String bar);
}
