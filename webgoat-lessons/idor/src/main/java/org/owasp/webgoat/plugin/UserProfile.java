package org.owasp.webgoat.plugin;

/**
 * Created by jason on 1/5/17.
 */
public class UserProfile {
    private String userId;
    private String name;
    private String color;
    private String size;
    private boolean isAdmin;
    // anyting else?

    public UserProfile() {}

    public UserProfile(String id) {
        this.userId = id;
        setProfileFromId(id);
    }

    //
    private void setProfileFromId(String id) {
        // emulate look up from database
        if (id.equals("2342384")) {
            this.color = "yellow";
            this.name = "Tom Cat";
            this.size = "small";
            this.isAdmin = false;
        } else if (id.equals("2342388")) {
            this.color = "brown";
            this.name = "Buffalo Bill";
            this.size = "large";
            this.isAdmin = false;
        }

    }

    public String toHTMLString() {
        String htmlBreak = "<br/>";
        return "userId" + this.userId + htmlBreak +
                "name" + this.name + htmlBreak +
                "size" + this.size + htmlBreak +
                "isAdmin" + this.isAdmin;
    }

    //
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
