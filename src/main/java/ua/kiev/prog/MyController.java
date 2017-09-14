package ua.kiev.prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MyController {

    private Map<Long, byte[]> photos = new HashMap<Long, byte[]>();

    @RequestMapping("/")
    public String onIndex(Model model) {
        if(photos.size() != 0){
            model.addAttribute("photos_id", photos.keySet());
        }
        return "index";
    }

    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    public String onAddPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo.isEmpty())
            throw new PhotoErrorException();

        try {
            long id = System.currentTimeMillis();
            photos.put(id, photo.getBytes());

            model.addAttribute("photo_id", id);
            return "result";
        } catch (IOException e) {
            throw new PhotoErrorException();
        }
    }

    @RequestMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping(value = "/view/{photo_id}")
    public String onView(Model model, @PathVariable ("photo_id") long id) {
        model.addAttribute("photo_id", id);
        return "result";
    }


    @RequestMapping("/delete/{photo_id}")
    public String onDelete(Model model, @PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            model.addAttribute("photos_id", photos.keySet());
        return "index";
    }

    @RequestMapping(value = "/photo/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> onDeleteAll(@RequestParam(value = "toDelete[]", required = false) long[] toDelete) {
        if (toDelete != null && toDelete.length > 0) {
            for (long id : toDelete)
                photos.remove(id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



    private ResponseEntity<byte[]> photoById(long id) {
        byte[] bytes = photos.get(id);
        if (bytes == null)
            throw new PhotoNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }
}
