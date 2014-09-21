package net.remisan.base.model;


public interface User extends PersistableEntity {

    Long getId();
    
    void setId(Long id);
    
    String getLogin();

    void setLogin(String login);

    String getEmail();

    void setEmail(String email);

    String getPlainPassword();

    void setPlainPassword(String password);
    
    String getPlainPasswordConfirmation();

    void setPlainPasswordConfirmation(String password);

}
