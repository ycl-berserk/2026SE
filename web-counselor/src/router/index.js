import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import LoginView from '../views/LoginView.vue'
import PendingListView from '../views/PendingListView.vue'
import ProcessedListView from '../views/ProcessedListView.vue'
import ReviewDetailView from '../views/ReviewDetailView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: LoginView },
    {
      path: '/',
      component: MainLayout,
      children: [
        { path: '', redirect: '/review/pending' },
        { path: 'review/pending', name: 'pending', component: PendingListView },
        { path: 'review/processed', name: 'processed', component: ProcessedListView },
        { path: 'review/:id', name: 'detail', component: ReviewDetailView },
      ],
    },
  ],
})

export default router
