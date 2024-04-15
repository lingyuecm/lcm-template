<script setup lang="ts">
import type { BizUserDto } from '@/model/dto'
import { onBeforeMount, ref } from 'vue'
import { getUsersApi } from '@/api/userApi'

const users = ref<BizUserDto[]>([])

function onRolesClick(user: BizUserDto) {
  alert(user.userId)
}

onBeforeMount(() => {
  getUsersApi({
    pageNo: 1,
    pageSize: 10
  })
    .then(response => users.value = response.resultBody.dataList)
})

function displayFullName(user: BizUserDto): string {
  return (user.firstName ? user.firstName : '') +
    (user.lastName ? (' ' + user.lastName) : '')
}
</script>

<template>
  <div>
    <el-table :data="users">
      <el-table-column
        label="Name">
        <template #default="scope">
          {{ displayFullName(scope.row as BizUserDto) }}
        </template>
      </el-table-column>
      <el-table-column
        label="Phone No."
        prop="phoneNo"/>
      <el-table-column
        label="Actions">
        <template #default="scope">
          <div @click="onRolesClick(scope['row'] as BizUserDto)">Roles</div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>

</style>