package distributed.transaction.controller;

import com.sun.deploy.net.URLEncoder;
import distributed.transaction.model.User;
import distributed.transaction.redis.RedisClient;
import distributed.transaction.utils.Constant;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/htmlTest")
//@CrossOrigin(allowCredentials = "true")
public class htmlController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping("/test/cookie")
    public String cookie(@RequestParam("browser") String browser, HttpServletRequest request, HttpSession session) {
        //取出session中的browser
        Object sessionBrowser = session.getAttribute("browser");
        if (sessionBrowser == null) {
            System.out.println("不存在session，设置browser=" + browser);
//            User user = new User("123");
            Map map = new HashMap();
            map.put("key", "123");
            map.put("token", "456");
            session.setAttribute("user", map);
        } else {
            System.out.println("存在session，browser=" + sessionBrowser.toString() + ">>>sessionId:" + session.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                System.out.println("cookie:" + cookie.getName() + " : " + cookie.getValue());
            }
        }
        return "/pages/index.html";
    }

    @RequestMapping("/getRedisSession")
    public String getRedisSession(HttpServletRequest request, HttpSession session) {
//        HttpSession session = request.getSession(false);
        redisTemplate.opsForHash().put("hashValue", "map1", "map1-1");
        redisTemplate.opsForHash().put("hashValue", "map2", "map2-2");
        List<Object> hashList = redisTemplate.opsForHash().values("hashValue");
        System.out.println("通过values(H key)方法获取变量中的hashMap值:" + hashList);
        Map<Object, Object> map = redisTemplate.opsForHash().entries("hashValue");
        System.out.println("通过entries(H key)方法获取变量中的键值对:" + map);
        Object mapValue = redisTemplate.opsForHash().get("hashValue", "map1");
        System.out.println("通过get(H key, Object hashKey)方法获取map键的值:" + mapValue);
        Set<Object> keySet = redisTemplate.opsForHash().keys("hashValue");
        System.out.println("通过keys(H key)方法获取变量中的键:" + keySet);
        long hashLength = redisTemplate.opsForHash().size("hashValue");
        System.out.println("通过size(H key)方法获取变量的长度:" + hashLength);
        if (session != null) {
            System.out.println("getRedisSession sessionId:" + session.getId());
            List<Object> hashList1 = redisTemplate.opsForHash().values("spring:session:sessions:" + session.getId());
            System.out.println(hashList1);
//            Set<String> keys = redisTemplate.keys("spring:session:sessions:*");
//            for (String key : keys) {
//                System.out.println("key:" + key);
//                if (key.indexOf("expires") == -1 && key.contains(session.getId())) {
////                    System.out.println("sessionAttr:" + redisTemplate.opsForHash().get(key, " sessionAttr:user"));
////                    System.out.println("maxInactiveInterval:" + redisTemplate.opsForHash().get(key, "maxInactiveInterval"));
//                    List<Object> hashList1 = redisTemplate.opsForHash().values(key);
//                    System.out.println(hashList1);
////                    BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(key);
////                    Object map1 = redisClient.get(key, Object.class);
////                    Set<Object> keySet1 = redisTemplate.opsForHash().keys(key);
////                    System.out.println(redisTemplate.opsForValue().get(key));
////                    Map<Object, Object> map1 = redisTemplate.opsForHash().entries(key);
////                    map1.forEach((k, v) -> {
////                        System.out.println("k:" + k);
////                        System.out.println("v:" + v);
////                    });
////                    System.out.println(1111);
//                    //                    System.out.println(redisTemplate.opsForHash().entries(key));
//                }
//            }
        }
        return "/pages/index.html";
    }

    @RequestMapping("/removeRedisSession")
    public String removeRedisSession(HttpServletRequest request, HttpSession session) {
        if (session != null) {
            System.out.println("removeRedisSession sessionId:" + session.getId());
//            session.removeAttribute("spring:session:sessions:" + session.getId());
            session.invalidate();
        }
        return "/pages/index.html";
    }

    @RequestMapping("/index")
    public String index() {
        return "/pages/index.html";
    }

    @RequestMapping("/{rootOrgId}/{id}/{userId}/{content}")
    public String previewRedirect(HttpServletRequest request, @PathVariable int rootOrgId, @PathVariable int id, @PathVariable int userId, @PathVariable String content) {
        request.getSession().setAttribute("aaa", "bbb");
        StringBuilder stringBuilder = new StringBuilder(Constant.COURSE_WARE_PPT_TRUE_PATH).append("/").append(rootOrgId).append("/").append(id).append("/").append(userId).append("/").append(content).append("/").append(Constant.INDEX_HTML);
        return "forward:" + stringBuilder.toString();
    }

    @RequestMapping("/previewRedirect")
    public ModelAndView previewRedirect() {
//        return new ModelAndView("http://www.baidu.com");
//        return "forward:http://www.baidu.com";
        return new ModelAndView(new RedirectView("http://www.baidu.com"));
    }

    @RequestMapping("/previewRedirect1")
    public ModelAndView previewRedirect1() {
        return new ModelAndView("http://www.baidu.com");
    }

    @RequestMapping("/{rootOrgId}/{courseId}/{lessonId}/{type}/{type1}")
    public String previewRedirect2(@PathVariable int rootOrgId, @PathVariable int courseId, @PathVariable int lessonId, @PathVariable int type, @PathVariable int type1) {
        String forwardStr = "/courseware/" + rootOrgId + "/" + courseId + "/" + lessonId + "/" + type + "/7.第七讲：受力分析 (Published)/index.html";
        return "forward:" + forwardStr;
    }

    @PostMapping("/previewRedirect3")
    public String previewRedirect3(@RequestBody User user) {
//        return "forward:/http://192.168.10.31:8012/courseware/639/87/175/5/7.第七讲：受力分析 (Published)/index.html";
        String forwardStr = "/courseware/" + user.getRootOrgId() + "/" + user.getCourseId() + "/" + user.getLessonId() + "/" + user.getType() + "/7.第七讲：受力分析 (Published)/index.html";
        return "forward:" + forwardStr;
    }

    @RequestMapping("/previewRedirect4")
    public String previewRedirect4(@RequestParam(required = true) int rootOrgId, @RequestParam(required = true) int courseId, @RequestParam(required = true) int lessonId, @RequestParam(required = true) int type) {
//        return "redirect:/courseware/639/87/175/5/7.第七讲：受力分析 (Published)/index.html";
        String forwardStr = "/courseware/" + rootOrgId + "/" + courseId + "/" + lessonId + "/" + type + "/7.第七讲：受力分析 (Published)/index.html";
        return "forward:" + forwardStr;
    }

    @RequestMapping("/previewRedirect5")
    public String previewRedirect5(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = encode("/courseware/639/284/55678/7.第七讲：受力分析 (Published)/index.html");
//        String url = "/courseware/639/284/55678/7.第七讲：受力分析 (Published)/index.html";
        request.getSession(false).setAttribute("aaa", "aaa");
//        return response.encodeRedirectURL(url);
//        response.sendRedirect(url);
//        return response.encodeURL(url);
//        return response.encodeRedirectURL(response.encodeURL(url));
//        return "redirect:/courseware/639/284/55678/" + URLEncoder.encode("7.第七讲：受力分析", "utf-8") + " (Published)/index.html";
        return "redirect:" + url;
//        return "forward:" + url;
//        return null;
    }

    @RequestMapping("/previewRedirect8")
    public String previewRedirect8(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("previewRedirect8 set session aaa value");
            session.setAttribute("aaa", "aaa");
        }
        return null;
    }

    @RequestMapping("/previewRedirect9")
    public String previewRedirect9(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("aaa") != null) {
                session.removeAttribute("aaa");
                System.out.println("previewRedirect9 remove session aaa value");
            }
        }
        return null;
    }

    @RequestMapping("/previewRedirect10")
    public String previewRedirect10(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null)
            System.out.println("previewRedirect10:" + session.getAttribute("aaa"));
        return null;
    }

    public String encode(String url) {
        try {
            Matcher matcher = Pattern.compile("[\\u4E00-\\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]").matcher(url);
            while (matcher.find()) {
                String tmp = matcher.group();
                url = url.replaceAll(tmp, java.net.URLEncoder.encode(tmp, "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * url重定向
     *
     * @param map
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/previewRedirect6")
    public ModelAndView previewRedirect6(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if (map == null)
//            throw new Exception("url不能为空");
//        String url = (String) map.get("url");
//        System.out.println(url);
//        url = url.replace("http://", "");
//        if (url.indexOf(":") != -1)
//            url = url.replace(url.substring(url.indexOf(":") + 1, url.indexOf("/")), String.valueOf(request.getServerPort()));
//        response.sendRedirect(url);
        String html = "http://192.168.10.31:8012/courseware/639/87/175/5/7.第七讲：受力分析 (Published)/index.html";
        String url1 = URLEncoder.encode(URLEncoder.encode(html, "utf-8"), "utf-8");
        String url2 = java.net.URLEncoder.encode(java.net.URLEncoder.encode(html, "utf-8"), "utf-8");
        String url3 = java.net.URLEncoder.encode(html, "utf-8");
        String url4 = URLEncoder.encode(html, "utf-8");
        return new ModelAndView(new RedirectView(url4));
    }

    @RequestMapping("/previewRedirect7")
    public void previewRedirect7(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.getRequestDispatcher("http://192.168.10.31:8012/courseware/639/87/175/5/7.第七讲：受力分析 (Published)/index.html").forward(request, response);
        IOUtils.copy(new FileInputStream("/Users/king/qmth/work_text/639/87/175/5/7.第七讲：受力分析 (Published)"), response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping("/testURL")
    public void testURL(HttpServletResponse response) throws Exception {
//        UrlResource resource = new UrlResource("http://localhost:8082/hello/courseware/639/87/175/5/7.第七讲：受力分析 (Published)/index.html");
//        if (resource.isReadable()) {
//            //URLConnection对应的getInputStream()。
//            printContent(resource.getInputStream());
//        }
//        response = getFile("/Users/king/qmth/work_text/639/87/175/5/7.第七讲：受力分析 (Published)", response);
//        response.flushBuffer();
//        String filePath = "/Users/king/qmth/work_text/639/87/175/5/7.第七讲：受力分析 (Published)";
//        FileSystemResource res1 = new FileSystemResource(filePath);
//        if (res1.exists()) {
//            System.out.println("资源的文件名：" + res1.getFilename());
//            System.out.println("资源的文件大小：" + res1.contentLength());
//            File file = res1.getFile();  //转换成Java的File对象
//
//            FileInputStream fis;
//            fis = new FileInputStream(file);
//
//            long size = file.length();
//            byte[] temp = new byte[(int) size];
//            fis.read(temp, 0, (int) size);
//            fis.close();
//            byte[] data = temp;
//            OutputStream out = response.getOutputStream();
//            out.write(data);
//            out.flush();
//            out.close();
//        } else {
//            System.out.println("指定资源不存在");
//        }
//        URL url = new URL("http://localhost:8082/hello/courseware/639/87/175/5/7.第七讲：受力分析 (Published)/index.html");
//        URLConnection conn = url.openConnection();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.
//                getInputStream()));
//        return getDocumentAt("http://localhost:8082/hello/courseware/639/87/175/5/7.第七讲：受力分析 (Published)/index.html");
//        File input = new File("/Users/king/qmth/work_text/639/87/175/5/7.第七讲：受力分析 (Published)/index.html");
//        Document doc = Jsoup.parse(input, "UTF-8");

//        response.setContentType("application/pdf");
//        //响应头部
//        response.setHeader("Content-disposition", "attachment;filename=" + "index" + ".html");
//
//        OutputStream oputstream = response.getOutputStream();
//        URL url = new URL("http://www.baidu.com");
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setDoInput(true);
//        urlConnection.connect();
//        InputStream inputStream = urlConnection.getInputStream();
//        byte[] buffer = new byte[4 * 1024];
//        int byteRead = 0;
//        //写入流中处理
//        while ((byteRead = (inputStream.read(buffer))) != -1) {
//            oputstream.write(buffer, 0, byteRead);
//        }
//        oputstream.flush();
//        inputStream.close();
//        oputstream.close();

        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName=index.html");
        ServletOutputStream out;
        // 通过文件路径获得File对象
        File html_file = new File("/Users/king/qmth/work_text/639/87/175/5/7.第七讲：受力分析 (Published)/");
        FileInputStream inputStream = new FileInputStream(html_file);

        // 3.通过response获取ServletOutputStream对象(out)
        out = response.getOutputStream();

        int b = 0;
        byte[] buffer = new byte[1024];
        while ((b = inputStream.read(buffer)) != -1) {
            // 4.写到输出流(out)中
            out.write(buffer, 0, b);
        }
        inputStream.close();
        out.flush();
        out.close();

//        return doc;
    }

//    public InputStream getFileStream() throws Exception{
//        System.out.println("/Users/king/qmth/work_text/639/87/175/5/7.第七讲：受力分析 (Published)");
//        String realPath = ServletActionContext.getServletContext().getRealPath("");
//        System.out.println("realPath:"+realPath);
//        return new FileInputStream(realPath+filename);
//    }

    public String getDocumentAt(String urlString) {
        StringBuffer document = new StringBuffer();
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.
                    getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                document.append(line + "\n");
            }
            reader.close();
        } catch (MalformedURLException e) {
            System.out.println("Unable to connect to URL: " + urlString);
        } catch (IOException e) {
            System.out.println("IOException when connecting to URL: " + urlString);
        }
        return document.toString();
    }

    public HttpServletResponse getFile(String path, HttpServletResponse response) throws IOException {
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                System.out.println("^^^^^" + array[i].getName());
                System.out.println("#####" + array[i]);
                System.out.println("*****" + array[i].getPath());
                IOUtils.copy(new FileInputStream(array[i].getPath()), response.getOutputStream());
            } else if (array[i].isDirectory()) {
                getFile(array[i].getPath(), response);
            }
        }
        return response;
    }

    public void printContent(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        if (is != null) {
            is.close();
        }
        if (br != null) {
            br.close();
        }
    }
}
