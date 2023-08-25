package rsarschoolmodel.com.rsarschoolmodel;

import java.io.Serializable;

public class ClassModel implements Serializable {


    private String Class_Name;

    private String Class_ID;





    public ClassModel(String class_Name,  String class_ID ) {
        this.Class_Name = class_Name;
        this.Class_ID = class_ID;

    }

    public String getClass_Name() {
        return Class_Name;
    }
    public void setClass_Name(String class_Name) {
        Class_Name = class_Name;
    }



    public String getClass_ID() {
        return Class_ID;
    }

    public void setClass_ID(String class_ID) {
        Class_ID = class_ID;
    }





}
