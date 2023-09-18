C.C.Online
==========

Online shopping mall,Centre commercial en ligne.

Le projet est réalisé avec une séparation du front-end et du back-end en programmant avec le lien de l'API RESTful.

Le portail du site web utilise Vue.js et Live Server.

La gestion pour l'administrateur est réalisée sous forme d'application monopage (SPA) basée sur Vue.js et Vuetify et est empaquetée avec Webpack.

Leyou Mall est un site web de commerce électronique (B2C) qui comprend une gamme complète de catégories.

Les utilisateurs peuvent rechercher des produits, faire des achats en ligne, ajouter des articles au panier, passer des commandes et les payer. Ils peuvent également suivre les commandes et laisser des commentaires.

Les administrateurs peuvent gérer l'ajout et la suppression de produits, effectuer des opérations CRUD sur les catégories, les marques, les produits et les spécifications.Des microservices sont utilisés pour prendre en charge un grand nombre de visites. 





### La structure du projet:

<img src="(https://github.com/roli1897/C.C.EnLigne/blob/master/image/931011b2-1356-4a62-b929-b449024ce54c.png)" title="" alt="931011b2-1356-4a62-b929-b449024ce54c" style="zoom:200%;">

### Le projet est divisé en 12 microservices :

![WeChat Image_20230917204730](https://github.com/roli1897/C.C.EnLigne/blob/master/image/WeChat%20Image_20230917204730.png)





### Host et configuration Nginx![WeChat Photo Editor_20230917195138](E:\C.C.EnLigne\image\WeChat%20Photo%20Editor_20230917195138.jpg)

<img src="file:///C:/Users/yuan6/Pictures/Typedown/a212ecfc-0064-4e21-8d8e-74a6030c23d1.png" title="" alt="a212ecfc-0064-4e21-8d8e-74a6030c23d1" style="zoom:150%;">  

<img title="" src="file:///C:/Users/yuan6/Pictures/Typedown/0270608d-07fd-42ad-998c-a6c5a4a9d2a3.png" alt="0270608d-07fd-42ad-998c-a6c5a4a9d2a3" style="zoom:150%;" data-align="left">

<img title="" src="file:///C:/Users/yuan6/Pictures/Typedown/5414e48c-f5be-41e6-8fd1-f1aedda31e4f.png" alt="5414e48c-f5be-41e6-8fd1-f1aedda31e4f" style="zoom:150%;">

<img src="file:///C:/Users/yuan6/Pictures/Typedown/11870820-a53a-4377-8f05-c2ac34b8b872.png" title="" alt="11870820-a53a-4377-8f05-c2ac34b8b872" style="zoom:150%;">

<img src="file:///C:/Users/yuan6/Pictures/Typedown/e1eb7cae-8061-4e7d-b9ae-f9bf230d593a.png" title="" alt="e1eb7cae-8061-4e7d-b9ae-f9bf230d593a" style="zoom:150%;">

### JWT＆RSA　Auth-service:



![Rsa](https://github.com/roli1897/C.C.EnLigne/blob/master/image/Rsa.png)



### Cart-service:

![cart](https://github.com/roli1897/C.C.EnLigne/blob/master/image/cart.png)



### Relation de "call"l entre le service d'article (item-service), le service de panier (cart-service) et le service de commande (order-service).

![item](https://github.com/roli1897/C.C.EnLigne/blob/master/image/item.png)

### L'interprétation de la technologie :

La combinaison des technologies mentionnées précédemment peut résoudre les problèmes typiques suivants dans le projet de commerce 

électronique :

* Node.js et Vue.js sont utilisés pour le développement du front-end et du back-end de manière séparée.
* Il s'appuie sur SpringBoot 2.0 et la version SpringCloud Finchley.RC1 pour mettre en œuvre les microservices.
* Les concepts de SPU (Unité de Produit Standard) et SKU (Unité de Stock Keeping) du secteur du commerce électronique sont utilisés pour améliorer la gestion des produits.
* Il résout le problème du stockage distribué des fichiers basé sur FastDFS.
* Il propose une recherche de filtre intelligent pour les produits de base basée sur la fonction d'agrégation avancée d'Elasticsearch.
* Il permet de réaliser des statistiques complexes et des rapports sur les résultats commerciaux des ventes grâce à la fonction d'agrégation avancée d'Elasticsearch.
* Il enregistre le panier d'achat du client sans ouvrir de session en utilisant Local Storage dans le but de réduire la charge sur le serveur.
* Il met en place une authentification unique sans état basée sur la technologie JWT (JSON Web Token) et le chiffrement asymétrique RSA.
* En combinaison avec JWT et RSA, un filtre Feign personnalisé peut effectuer l'authentification automatique entre les services et résoudre le problème de sécurité de l'exposition des services.
* Il utilise le service de SMS Alidayu pour résoudre le problème des notifications par SMS.
* Un service de messagerie fiable basé sur RabbitMQ est mis en place pour résoudre les problèmes de transactions distribuées et de communication entre les services.
* Le SDK WeChat est utilisé pour effectuer les paiements par scan de code QR avec WeChat.
* Un cluster haute disponibilité basé sur Redis est créé pour assurer un stockage en cache fiable des "données chaudes".
* Il repose sur Redis et Mq pour des scénarios à haute disponibilité et haute concurrence.
* Une page statique basée sur Thymeleaf est créée pour améliorer la vitesse de réponse de la page et la capacité de concurrence.
* Une répartition de charge des demandes initiales et une limitation du débit des demandes sont mises en place en utilisant Nginx.
  
  
  
  

### Administration:

![7bcde59e-5dbc-4bfb-955c-f5b6f806cb49](https://github.com/roli1897/C.C.EnLigne/blob/master/image/WeChat%20Screenshot_20230916225609.png)

#### Page de rechercher

![5a34c1e7-9d55-47a1-95d3-0bdf43509658](file:///C:/Users/yuan6/Pictures/Typedown/5a34c1e7-9d55-47a1-95d3-0bdf43509658.png)

#### Page du panier

![c404042d-ff7e-49be-a955-a456adb100db](file:///C:/Users/yuan6/Pictures/Typedown/c404042d-ff7e-49be-a955-a456adb100db.png)


