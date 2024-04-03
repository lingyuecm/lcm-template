<template>
  <div class="flex h-full bg-blue-200">
    <div :class="sidebarExpanded ? 'sidebar-expanded' : 'sidebar-narrow'" @click="toggleSidebar">Sidebar</div>
    <div :class="sidebarExpanded ? 'content-normal' : 'content-wide'">
      <div class="w-full h-[5rem] bg-blue-200">Top</div>
      <div class="w-full h-[calc(100%-5rem)] bg-yellow-200">
        <router-view/>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useSidebarStore } from '@/stores/sidebar'
import { storeToRefs } from 'pinia'

const sidebarStore = useSidebarStore()
const { sidebarExpanded } = storeToRefs(sidebarStore)
const toggleSidebar = () => {
  sidebarStore.toggleSidebar()
}
</script>

<style scoped>
@import 'windowFrame.css';
</style>