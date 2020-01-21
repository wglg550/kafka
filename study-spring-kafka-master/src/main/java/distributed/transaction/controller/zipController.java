package distributed.transaction.controller;

import distributed.transaction.utils.Constant;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;

@RestController
@RequestMapping("/zipTest")
//@CrossOrigin(allowCredentials = "true")
public class zipController {

    @ResponseBody
    @PostMapping(value = "/uploadCourseWare")
    public URL uploadCourseWare(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = request.getParameterMap();
        Long rootOrgId = Long.parseLong(org.apache.commons.lang.StringUtils.join((String[]) map.get("rootOrgId")));
        Long id = Long.parseLong(org.apache.commons.lang.StringUtils.join((String[]) map.get("id")));
        Long userId = Long.parseLong(org.apache.commons.lang.StringUtils.join((String[]) map.get("userId")));
        StringBuilder destPath = new StringBuilder(Constant.ZIP_PPT_PATH);
        destPath.append("/").append(rootOrgId).append("/").append(id).append("/").append(userId);

        URL url = null;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        if (!multipartFile.isEmpty()) {
            File file = new File(multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
//            CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) multipartFile;
//            DiskFileItem diskFileItem = (DiskFileItem) commonsmultipartfile.getFileItem();
//            File file = (File) multipartFile;
//            File file = diskFileItem.getStoreLocation();
            String path = unZip(file.getPath(), destPath.toString(), Constant.INDEX_HTML);
            String projectName = request.getContextPath();
            if (path != null)
                url = new URL(new StringBuilder("http://").append(request.getServerName()).append(":").append(request.getServerPort()).append(projectName).append(Constant.COURSE_WARE_PPT_TRUE_PATH).append("/").append(path.replace(Constant.ZIP_PPT_PATH, "")).toString());
        }
        return url;
    }

    @ResponseBody
    @GetMapping(value = "/uploadCourseWare1")
    public void uploadCourseWare1(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        System.out.println("uploadCourseWare1 sessionId:" + session.getId());
        session.setAttribute("test2", "test2");
    }

    public String unZip(String srcPath, String dest, String indexPath) throws Exception {
        String path = null;
        File file = new File(srcPath);
        if (!file.exists())
            throw new RuntimeException(srcPath + "所指文件不存在");
        ZipFile zf = new ZipFile(file);
        Enumeration entries = zf.getEntries();
        ZipEntry entry = null;
        boolean includeIndex = false;
        while (entries.hasMoreElements()) {
            entry = (ZipEntry) entries.nextElement();
            System.out.println("解压" + entry.getName());
            if (entry.isDirectory()) {
                String dirPath = dest + File.separator + entry.getName();
                File dir = new File(dirPath);
                dir.mkdirs();
            } else {
                // 表示文件
                File f = new File(dest + File.separator + entry.getName());
                if (!f.exists()) {
                    String dirs = f.getParentFile().getPath();
                    File parentDir = new File(dirs);
                    parentDir.mkdirs();
                }
                f.createNewFile();
                if (indexPath != null && !includeIndex && f.getName().trim().indexOf(indexPath) != -1) {
                    path = f.getPath();
                    includeIndex = true;
                }
                // 将压缩文件内容写入到这个文件中
                InputStream is = zf.getInputStream((ZipArchiveEntry) entry);
                FileOutputStream fos = new FileOutputStream(f);

                int count;
                byte[] buf = new byte[8192];
                while ((count = is.read(buf)) != -1) {
                    fos.write(buf, 0, count);
                }
                is.close();
                fos.close();
            }
        }
        if (indexPath != null && !includeIndex)
            throw new Exception("该zip文件里没有找到index.html");
        return path;
    }
}
