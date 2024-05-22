<script setup lang="ts">
import type { Menu } from '@/model/model'
import { ArrowDown } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

type SidebarItemProps = {
  item: Menu,
  indent: number
}
const router = useRouter()
const route = useRoute()
const props = defineProps<SidebarItemProps>()

const subMenuExpanded = ref(false)
function onSubMenuClick() {
  subMenuExpanded.value = !subMenuExpanded.value
}

function isCurrentMenu(path: string): boolean {
  return (path === route.path) || (
    (route.redirectedFrom !== undefined) && (path === route.redirectedFrom.path)
  )
}

function onMenuItemClick(path: string) {
  router.push({
    path: path
  })
}
</script>

<template>
  <div>
    <div v-if="props.item.children && props.item.children.length > 0">
      <div :style="{
      textIndent: props.indent + 'ch'
    }" class="sidebar-item-base"
      @click="onSubMenuClick">
        <span>{{ props.item.menuTitle }}</span>
        <el-icon
          :class="subMenuExpanded ? 'sub-menu-arrow sub-menu-arrow-expanded' : 'sub-menu-arrow'">
          <ArrowDown/>
        </el-icon>
      </div>
      <div v-show="subMenuExpanded">
        <SidebarItem
          v-for="(child, index) in props.item.children"
          :key="'Sub-Menu-' + index"
          :item="child"
          :indent="props.indent + 2"/>
      </div>
    </div>
    <div v-else>
      <div
        :style="{textIndent: props.indent + 'ch'}"
        :class="isCurrentMenu(props.item.menuUrl) ? 'sidebar-item-base sidebar-item-current' : 'sidebar-item-base'"
        @click="onMenuItemClick(props.item.menuUrl)">
        <span>{{ props.item.menuTitle }}</span>
      </div>
    </div>
    <div class="w-full h-[1px] bg-blue-300"></div>
  </div>
</template>

<style scoped>
@import 'sidebarItem.css';
</style>