var menus = [
  {
    action: "home",
    title: "page d'accueil",
    path:"/index",
    items: [{ title: "statistique", path: "/dashboard" }]
  },
  {
    action: "apps",
    title: "gestion des produits",
    path:"/item",
    items: [
      { title: "gestion de la catégorisation", path: "/category" },
      { title: "gestion de la marque", path: "/brand" },
      { title: "liste de produits", path: "/list" },
      { title: "spécifications techniques", path: "/specification" }
    ]
  },
  {
    action: "people",
    title: "gestion des membres",
    path:"/user",
    items: [
      { title: "statistiques des membres", path: "/statistics" },
      { title: "gestion des membres", path: "/list" }
    ]
  },
  {
    action: "attach_money",
    title: "gestion des ventes",
    path:"/trade",
    items: [
      { title: "statistiques de transactions", path: "/statistics" },
      { title: "gestion des commandes", path: "/order" },
      { title: "gestion logistique", path: "/logistics" },
      { title: "gestion de la promotion", path: "/promotion" }
    ]
  },
  {
    action: "settings",
    title: "gestion des autorisations",
    path:"/authority",
    items: [
      { title: "gestion des autorisations", path: "/list" },
      { title: "gestion des rôles", path: "/role" },
      { title: "gestion du personnel", path: "/member" }
    ]
  }
]

export default menus;
