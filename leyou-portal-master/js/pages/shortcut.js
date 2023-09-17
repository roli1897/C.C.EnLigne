const shortcut = {
    template: "\
    <div class='py-container'> \
        <div class='shortcut'> \
            <ul class='fl'> \
               <li class='f-item'>Bienvenue chez LEYOU!</li> \
               <li class='f-item' v-if='user && user.username'>\
               尊敬的会员，<span style='color: red;'>{{user.username}}</span>\
               </li>\
               <li v-else class='f-item'> \
                   <a href='javascript:void(0)' @click='gotoLogin'>Connexion</a>　 \
                   <span><a href='/register.html' target='_blank'>S'inscrire</a></span> \
               </li> \
           </ul> \
           <ul class='fr'> \
               <li class='f-item'>Mes commands</li> \
               <li class='f-item space'></li> \
               <li class='f-item'><a href='/home.html' target='_blank'>mon leyou</a></li> \
               <li class='f-item space'></li> \
               <li class='f-item'>Membre leyou</li> \
               <li class='f-item space'></li> \
               <li class='f-item'>Achat groupé</li> \
               <li class='f-item space'></li> \
               <li class='f-item'>Concentrer</li> \
               <li class='f-item space'></li> \
               <li class='f-item' id='service'> \
                   <span>Service client</span> \
                   <ul class='service'> \
                       <li><a href='/cooperation.html' target='_blank'>合作招商</a></li> \
                       <li><a href='/shoplogin.html' target='_blank'>商家后台</a></li> \
                       <li><a href='/cooperation.html' target='_blank'>合作招商</a></li> \
                       <li><a href='#'>商家后台</a></li> \
                   </ul> \
               </li> \
               <li class='f-item space'></li> \
               <li class='f-item'>Navigation</li> \
           </ul> \
       </div> \
    </div>\
    ",
    name: "shortcut",
    data() {
        return {
            user: null
        }
    },
    created() {
        ly.http("/auth/verify")
            .then(resp => {
                this.user = resp.data;
            })
    },
    methods: {
        gotoLogin() {
            window.location = "/login.html?returnUrl=" + window.location;
        }
    }
}
export default shortcut;