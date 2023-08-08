package oeg.crec.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import oeg.crec.Status;
import oeg.crec.model.Course;
import oeg.crec.store.Courses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * https://ai4labour.linkeddata.es/crec/api/search?q=ADN
 * @author victor
 */
@RestController
@RequestMapping(value = "/api/search")
@Api(tags = "search", value = "Searches for courses.", description = "Internal methods.")
public class SearchController {
    
    @ApiOperation(value = "Searches a course.", notes = "")
    @GetMapping(value = "", produces = "application/json")
    @ResponseBody
    public List<Course> search(
    @ApiParam(name="q", value="Searches a course by LO",  example="") @RequestParam(defaultValue="", required = false) String q
    ) {
        List<Course> courses = Courses.search(q);
        return courses;
    }
     
}
