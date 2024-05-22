<script setup lang="ts">
import { useSidebarStore } from '@/stores/sidebar'
import { storeToRefs } from 'pinia'
import { useMenuStore } from '@/stores/menu'
import SidebarItem from '@/components/frame/SidebarItem.vue'
import { ArrowLeft, Key, Refresh } from '@element-plus/icons-vue'
import { usePersonStore } from '@/stores/person'
import { onBeforeMount, ref } from 'vue'
import { logoutApi, metadataApi } from '@/api/userApi'
import { removeAccessToken } from '@/utils/cacheManager'
import { refreshPermissionsApi } from '@/api/permissionApi'

const sidebarStore = useSidebarStore()
const { sidebarExpanded } = storeToRefs(sidebarStore)

const personStore = usePersonStore()
const menuStore = useMenuStore()

const refreshing = ref<Boolean>(false);

const toggleSidebar = () => {
  sidebarStore.toggleSidebar()
}

const getFullName = () => {
  const personName = personStore.personName

  return (personName.firstName ? personName.firstName : '') +
    (personName.lastName ? (' ' + personName.lastName) : '')
}

const logout = () => {
  logoutApi()
    .then(() => {
      removeAccessToken()
      window.location.reload()
    })
}

const refreshPermissions = () => {
  if (refreshing.value) {
    return
  }
  refreshing.value = true
  refreshPermissionsApi()
    .then(() => {
      refreshing.value = false
    })
}

onBeforeMount(() => {
  if (!personStore.personName.firstName && !personStore.personName.lastName) {
    metadataApi()
      .then(response => {
      personStore.setPersonName({
        firstName: response.resultBody.firstName,
        lastName: response.resultBody.lastName
      });
      menuStore.setMenuTree(response.resultBody.grantedMenus);
    }).catch(() => {})
  }
})
</script>
<template>
  <div class="flex h-full">
    <div :class="sidebarExpanded ? 'sidebar-expanded' : 'sidebar-normal'">
      <div class="h-[5rem]">
        <div
          :style="{display: sidebarExpanded ? 'inline-block' : 'none', borderRadius: '50%'}"
          :class="refreshing ? 'refresh-permissions-button refresh-refreshing' : 'refresh-permissions-button'"
          @click="refreshPermissions">
          <el-icon id="key">
            <Key/>
          </el-icon>
          <el-icon id="refresh">
            <Refresh/>
          </el-icon>
        </div>
        <div class="flex float-right w-[5rem] h-full items-center hover:bg-blue-300" @click="toggleSidebar">
          <div class="w-full text-center hover:cursor-pointer">
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
      <div class="flex w-full h-[5rem] bg-orange-200">
        <div class="flex w-fit h-full text-[1.5rem] items-center">Hello, <span class="text-[1.8rem] font-bold">{{ getFullName() }}</span></div>
        <div
          class="absolute flex right-[1rem] h-[5rem] float-right items-center text-[1.5rem] pl-[1rem] pr-[1rem] hover:bg-orange-300 hover:cursor-pointer"
          @click="logout">Logout</div>
      </div>
      <div class="w-full h-[calc(100%-5rem)] p-[0.2rem]">
        <router-view/>
      </div>
    </div>
  </div>
</template>
<style scoped>
@import 'windowFrame.css';
</style>