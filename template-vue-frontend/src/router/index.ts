import { createRouter, createWebHistory } from 'vue-router'
import WindowFrame from '../components/frame/WindowFrame.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: WindowFrame,
      children: [
        {
          path: '/',
          redirect: '/dashboard'
        },
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('../views/dashboard/Dashboard.vue')
        }
      ]
    }
  ]
})

export default router
