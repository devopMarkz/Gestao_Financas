import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import authService from '@/services/authService'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'LoginView',
    component: LoginView
  },
  {
    name: 'DashboardView',
    path: '/dashboard',
    component: () => import('@/views/DashboardView.vue'),
    meta: { requiresAuth: true }
  },
  {
    name: 'CategoriasView',
    path: '/categorias',
    component: () => import('@/views/CategoriasView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '*',
    redirect: '/login'
  },
  {
    name: 'CriarContaView',
    path: '/conta',
    component: () => import('@/views/CriarContaView.vue')
  },
  {
    name: 'ContasRecorrentesView',
    path: '/contas-recorrentes',
    component: () => import('@/views/ContasRecorrentesView.vue')
  },
  {
    name: 'GraficoReceitaDespesaView',
    path: '/grafico-analise',
    component: () => import('@/views/GraficoReceitaDespesaView.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!authService.isAuthenticated()) {
      next({path: '/login', replace: true});
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router