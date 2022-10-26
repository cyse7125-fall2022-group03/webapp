package cyse7125.fall2022.group03.service.Impl;

import cyse7125.fall2022.group03.service.CommentService;

public class CommentServiceImpl implements CommentService {

    
    //createComment - taken care in Repository file

    /*    @Autowired
    TagRepository tagRepository;
    
    @Autowired
    UserServiceImpl userServiceImpl;
    
    @Autowired
    TaskServicceImpl taskServicceImpl;
    
    @Override
    public ResponseEntity<JSONObject> createTag(Tag newTag) {
        
        ResponseEntity<JSONObject> result;
        try {
            
            User user = userServiceImpl.getCurrentUser();
            Task task = taskServicceImpl.getc
            
            Tag tagToPut = new Tag(user.getId(), newTag.getName(), String.valueOf(new Date()), String.valueOf(new Date()));
            
            newTag = tagRepository.save(tagToPut);
            
    
        }catch (Exception e){
            e.printStackTrace();
            return generateResponse(null, HttpStatus.BAD_REQUEST);  //null ?
        }
        
        return generateResponse(newTag, HttpStatus.CREATED);
    }
    
    
    
    public ResponseEntity<JSONObject> generateResponse(Object messageObject, HttpStatus status) {
        if(messageObject instanceof List<?>) {
            if (((List<?>) messageObject).size()==0) {
                messageObject = ((List<?>) messageObject).get(0);
            } else {
                JSONObject obj2=new JSONObject();
                obj2.put("List", messageObject);
                messageObject = obj2;
            }
        }
        
        return new ResponseEntity<>(JSONObject.parseObject(JSON.toJSONString(messageObject)),status);
    }
    public ResponseEntity<JSONObject> generateResponse(String messageString, HttpStatus status) {
        
        return new ResponseEntity<>(JSONObject.parseObject(messageString),status);
    }
*/
}
