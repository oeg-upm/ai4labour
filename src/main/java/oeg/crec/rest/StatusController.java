package oeg.crec.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import oeg.crec.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Version and internal information method.
 * @author vroddon
 */
@RestController
@RequestMapping(value = "/api/status")
@Api(tags = "status", value = "Shows internal information. This method is only to be consumed for advanced users.", description = "Internal methods.")
public class StatusController {    
    @ApiOperation(value = "Describes the status of the service.", notes = "Different types of status can be retrieved. <br>Default information: Application version, health of the ElasticSearch store<br>Log information: latest queries")
    @GetMapping(value = "", produces = "application/json")
    @ResponseBody
    public Status statusget(
    @ApiParam(name="value", value="Provides status information",  example="") @RequestParam(defaultValue="", required = false) String value
    ) {
        return Status.getStatus();
    }
    
    //NOT WORKING BECAUSE INDIVIDUAL ACCOUNTS IN REPREP ARE NCESSSARY TO INSTALL GITHUB CERTIFICATE
    @ApiOperation(value = "deploys the application.", hidden = true)
    @RequestMapping(value = "/deploy", method = RequestMethod.GET)
    @ResponseBody
    public String deploy() {
        System.out.println("Deploying!");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/crec/deploy.sh");
        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                System.out.println(output);
//                System.exit(0);
            } else {
                //abnormal...
                System.err.println("error " + exitVal);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Deployed";
    }    
}
