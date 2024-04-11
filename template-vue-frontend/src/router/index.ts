import { createRouter, createWebHistory } from 'vue-router'
import WindowFrame from '../components/frame/WindowFrame.vue'
import { getAccessToken } from '@/utils/cacheManager'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      component: () => import('../views/login/UserLogin.vue')
    },
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
          component: () => import('../views/dashboard/HomeDashboard.vue')
        }
      ]
    },
    {
      path: '/backendMgmt',
      component: WindowFrame,
      children: [
        {
          path: 'userMgmt',
          component: () => import('../views/management/UserManagement.vue')
        },
        {
          path: 'roleMgmt',
          component: () => import('../views/management/RoleManagement.vue')
        },
        {
          path: 'permissionMgmt',
          component: () => import('../views/management/PermissionManagement.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (getAccessToken()) {
    next()
  } else if (to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router
