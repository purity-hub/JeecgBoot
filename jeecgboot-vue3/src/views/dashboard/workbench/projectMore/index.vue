<script setup lang="ts">
import { ref } from 'vue';
import { Card } from 'ant-design-vue';
import { Icon } from '/@/components/Icon';

const projects = ref([
  { title: '项目1', icon: 'ion:home', color: '#1890ff', desc: '项目1描述', group: '组1', date: '2023-10-01' },
  { title: '项目2', icon: 'ion:briefcase', color: '#52c41a', desc: '项目2描述', group: '组2', date: '2023-10-02' },
  // 更多项目数据...
]);
</script>

<template>
  <Card title="更多项目">
    <div class="flex flex-wrap gap-4">
      <template v-for="item in projects" :key="item.title">
        <CardGrid class="!w-[calc(25%-1rem)]">
          <span class="flex">
            <Icon :icon="item.icon" :color="item.color" size="30" />
            <span class="text-lg ml-4">{{ item.title }}</span>
          </span>
          <div class="flex mt-2 h-10 text-secondary"> {{ item.desc }} </div>
          <div class="flex justify-between text-secondary">
            <span>{{ item.group }}</span>
            <span>{{ item.date }}</span>
          </div>
        </CardGrid>
      </template>
    </div>
  </Card>
</template>

<style scoped lang="less">
// 可以根据需要添加样式
</style>