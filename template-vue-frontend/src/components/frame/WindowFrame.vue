<script setup lang="ts">
import { useSidebarStore } from '@/stores/sidebar'
import { storeToRefs } from 'pinia'
import { useMenuStore } from '@/stores/menu'
import SidebarItem from '@/components/frame/SidebarItem.vue'
import { ArrowLeft, ArrowLeftBold, DArrowLeft } from '@element-plus/icons-vue'

const sidebarStore = useSidebarStore()
const { sidebarExpanded } = storeToRefs(sidebarStore)
const toggleSidebar = () => {
  sidebarStore.toggleSidebar()
}

const menuStore = useMenuStore()
</script>
<template>
  <div class="flex h-full">
    <div :class="sidebarExpanded ? 'sidebar-expanded' : 'sidebar-normal'">
      <div class="h-[5rem]">
        <div :style="{display: sidebarExpanded ? 'inline-block' : 'none'}" class="w-[5rem] h-full hover:bg-blue-300">AAA</div>
        <div class="flex float-right w-[5rem] h-full items-center hover:bg-blue-300" @click="toggleSidebar">
          <div class="w-full text-center">
            <el-icon :class="sidebarExpanded ? 'sidebar-toggle-arrow' : 'sidebar-toggle-arrow sidebar-toggle-arrow-flipped'">
              <ArrowLeft/>
            </el-icon>
          </div>
        </div>
      </div>
      <SidebarItem
        v-for="(item, index) in menuStore.menuTree"
        :key="'Menu-' + index"
        :item="item"
        :indent="0"/>
    </div>
    <div :class="sidebarExpanded ? 'content-normal' : 'content-wide'">
      <div class="w-full h-[5rem] bg-orange-200">Top</div>
      <div class="w-full h-[calc(100%-5rem)] bg-yellow-200">
        <router-view/>
      </div>
    </div>
  </div>
</template>
<style scoped>
@import 'windowFrame.css';
</style>