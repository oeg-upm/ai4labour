package oeg.crec.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import oeg.crec.Status;
import oeg.crec.model.Course;
import oeg.crec.store.Courses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/api/course")
@Api(tags = "search", value = "Searches for courses.", description = "Internal methods.")
public class CourseController {
    
    @ApiOperation(value = "Gets a course.", notes = "")
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public Course get(@PathVariable String id) {
        Course courses = Courses.get(id);
        return courses;
    }

    @ApiOperation(value = "Searches a course.", notes = "")
    @GetMapping(value = "/search", produces = "application/json")
    @ResponseBody
    public List<Course> search(
    @ApiParam(name="q", value="Searches a course by LO",  example="") @RequestParam(defaultValue="", required = false) String q
    ) {
        List<Course> courses = Courses.search(q, 5);
        return courses;
    }
    
    
    
     
}
