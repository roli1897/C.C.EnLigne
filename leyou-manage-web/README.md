# leyou-manage-web

> **Vue.js projet**



**VUE.JS**

NPM (Node Package Manager) est un outil de gestion de modules fourni par Node.js. Il permet de télécharger et d'installer facilement de nombreux frameworks front-end tels que jQuery, AngularJS et Vue.js.

Pour commencer, on a besoin d'installer node.js



## Build Setup

```bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).



1.树组件的用法

## 1.1.示例

    <v-tree url="/item/category/list"
            :treeData="treeData"
            :isEdit="true"
            @handleAdd="handleAdd"
            @handleEdit="handleEdit"
            @handleDelete="handleDelete"
            @handleClick="handleClick"
    />
    
    
    <script>
      import {treeData} from "../../mockDB";
    
      export default {
        name: "category",
        data() {
          return {
            isEdit:true,
            treeData
          }
        },



effet:

![1526003587571](E:\leyou-manage-web\1526003587571.png)






