package br.com.ema;

import org.litepal.crud.DataSupport;

import java.util.List;

import br.com.ema.entities.Challenge;
import br.com.ema.entities.Student;

public class QueryChallengeTest extends EMATestCase {

    Student sam;
    Challenge c1;

    public void testGetAll(){
        DataSupport.deleteAll(Student.class);
        DataSupport.deleteAll(Challenge.class);
        sam = new Student();
        sam.setNickname("sam");
        sam.setPassword("1234");
        c1 = new Challenge();
        c1.setName("challenge 1");
        c1.setText("abc");
        c1.setImagePath("/image");
        sam.getChallenges().add(c1);
        c1.getStudents().add(sam);
        sam.save();
        sam.update(sam.getId());
        sam.save();
        c1.save();
        c1.update(c1.getId());
        c1.save();
        List<Student> students = DataSupport.findAll(Student.class,true);
        assertTrue(students.size() > 0);
    }

}
