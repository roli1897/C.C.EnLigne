package com.leyou.upload.test;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FastDFSTest {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Test
    public void testUpload() throws FileNotFoundException {
        // le fichier à télécharger
        File file = new File("C:\\Users\\yuan6\\source\\repos\\es.jpg");
        // Télécharger et enregistrer l'image, paramètres : 1-Flux du fichier téléchargé 2-Taille du fichier 3-Extension du fichier 4-Peut être ignoré
        StorePath storePath = this.storageClient.uploadFile(
                new FileInputStream(file), file.length(), "jpg", null);
        // chemin avec groupe
        System.out.println(storePath.getFullPath());
        // chemin sans groupe
        System.out.println(storePath.getPath());
    }

    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("C:\\Users\\yuan6\\source\\repos\\es.jpg");
        // Télécharger et générer une vignette"
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "png", null);
        // chemin avec groupe
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
        // chemin sans groupe
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(path);
    }
}