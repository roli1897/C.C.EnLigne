package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * charger une image
     * @param file
     * @return
     */
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/jpg");
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        //vérifier le type de fichier
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)) {
            //type illégal，return null
            LOGGER.info("type de fichier illégal: {}", originalFilename);
            return null;
        }
        try {
            //verificar el contenido del archivo
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("contenu de fichier illégal: {}", originalFilename);
                return null;
            }
            //enregistrer sur le serveur
//            file.transferTo(new File("F:\\ideawork\\leyoumall\\image\\" + originalFilename));
            //utiliser FastDFS pour charger une image
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            //retourner l'adresse URL
//            return "http://image.leyou.com/" + originalFilename;
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("error interno de servidor" + originalFilename);
            e.printStackTrace();
        }
        return null;
    }

}