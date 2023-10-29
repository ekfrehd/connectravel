package com.connectravel.manager;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


@Data
@Component
public class FileManager {

    private String dir;
    private static final AtomicInteger counter = new AtomicInteger(0);

    //생성자에 경로를 입력하면 입력한 경로에 저장
    public FileManager (String dir){
        this.dir = dir;
    }
    
    //생성자에 경로를 입력 안할시 기본 경로를 사용해서 저장
    public FileManager (){
        dir = System.getProperty("user.dir") + "\\imgtest";
        System.out.println(dir);
    }

    //파일 삭제 메서드
    public boolean remove(String filename){
        Autofolder(dir);
        File file = new File(dir + filename);
        return file.delete();
    }

    //멀티파트 파일과 파일이름을 입력하면 자동으로 파일 생성 (파일이 없을시 저장안함)
    public boolean add(MultipartFile file,String filename){
        if(check_extension(filename)){
            Autofolder(dir); //자동 폴더 생성코드
            File addfile = new File(dir,filename); //파일 경로/ 생성할 파일 이름
            try{
                file.transferTo(addfile); //파일 저장
            }
            catch (Exception e){
                System.out.println("파일 저장 시스템에서 오류 발생1");
            }
            return true;
        }
        else{
            return false;
        }
    }

    //멀티파트 파일과 파일이름을 입력하면 자동으로 파일 생성 (리스트 형식의 데이터 png,jpg) 만 저장할수 있도록 설정할수 있음
    public boolean add(MultipartFile file,String filename,List<String> CheckList){
        if(check_extension(filename,CheckList)){
            Autofolder(dir);
            File addfile = new File(dir,filename);
            try{
                file.transferTo(addfile); //파일 저장
            }
            catch (Exception e){
                System.out.println("파일 저장 시스템에서 오류 발생2");
            }
            return true;
        }
        else{
            return false;
        }
    }

    //자동 폴더 생성 기능
    public void Autofolder(String folder_name) {
        File Folder = new File(folder_name);
        if (!Folder.exists()) {
            try {
                Folder.mkdir(); //폴더 생성
                System.out.println("폴더가 생성완료.");
            } catch (Exception e) {
                System.out.println("폴더 생성에서 오류가 발생하였습니다.");
                e.getStackTrace();
            }
        } else {
            System.out.println("폴더가 이미 존재합니다..");
        }
    }

    //파일 확장자 검출 기능 filename = 검사할 파일 이름 / CheckList = 체크할 항목(여기 있는 항목의 확장자만 통과됨)
    public boolean check_extension(String filename, List<String> CheckList){
        String extension = StringUtils.getFilenameExtension(filename); //파일 확장자 검출
        return CheckList.contains (extension); //-1일시 체크리스트에 없는 확장자이므로 false를 리턴해서 파일 업로드를 제한함.
    }

    //쓰레기파일 검출
    public boolean check_extension(String filename){
        String extension = StringUtils.getFilenameExtension(filename); //파일 확장자 검출
        return extension != null; //파일 확장자가 없을때는 쓰레기 파일이 되므로 기본적으로 검출이 필요함
    }

    //UUID 생성(파일이름이 겹치지 않게)
    public String UUIDMaker(String filename){
        String uuid = UUID.randomUUID().toString(); //UUID 생성
        String realname = uuid + "_" +  filename; //서로 합침
        return realname;
    }

    public String generateFileName(String origin) {
        String extension = "";
        int dotIndex = origin.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < origin.length() - 1) {
            extension = origin.substring(dotIndex + 1);
        }
        int currentCount = counter.getAndIncrement();
        String realname = currentCount + "." + extension;
        return realname;
    }

}
