package oeg.crec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the internal state of the crecportal.
 * @author vroddon
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Status {
    
    private boolean linux;
    
    public static void main(String args[])
    {
        Status org = Status.getStatus();
        System.out.println(org);
    }
    
    public static Status getStatus()
    {
        Status status = new Status();
        return status;
    }
    
    public boolean getLinux()
    {
        return Main.LINUX;
    }
    
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(this);
            return jsonString;
        } catch (Exception e) {
            return "";
        }
    }    


}
