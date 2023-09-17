package com.leyou.goods.service;

import com.leyou.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {
    @Autowired
    private TemplateEngine engine;
    @Autowired
    private GoodsService goodsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);

    /**
     * créer le page de html
     * @param spuId
     */
    public void createHtml(Long spuId) {
        //Initialiser le contexte d'exécution
        Context context = new Context();
        //Configurer le modèle de données
        context.setVariables(this.goodsService.loadData(spuId));
        PrintWriter printWriter = null;
        try {
            //Transférer des fichiers statiques vers le serveur local
            File file = new File("E:\\Tools\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
            printWriter = new PrintWriter(file);
            this.engine.process("item", context, printWriter);
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la création de pages statiques : {}" + e, spuId );
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * Créer un nouveau thread pour manipuler les pages statiques
     * @param spuId
     */
    public void asyncExecute (Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
    }


    public void deleteHtml(Long id) {
        File file = new File("E:\\Tools\\nginx-1.14.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }
}