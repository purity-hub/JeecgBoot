<template>
  <a-layout class="container">
    <!-- 侧边栏 -->
    <a-layout-sider theme="light" width="280" class="sider">
      <div class="toolbar">
        <a-button type="primary" :loading="isLoading" @click="selectDirectory">打开文件夹</a-button>
      </div>
      <a-tree
        v-if="fileTree.length"
        :tree-data="fileTree"
        :expanded-keys="expandedKeys"
        :selected-keys="selectedKeys"
        @expand="handleExpand"
        @select="handleSelect"
      >
        <template #title="{ title, isDir, key }">
          <span class="tree-node">
            <component :is="isDir ? FolderOutlined : FileOutlined" />
            <span class="name">{{ title }}</span>
          </span>
        </template>
      </a-tree>
    </a-layout-sider>

    <!-- 主内容区 -->
    <a-layout-content class="content">
      <div v-if="selectedFileContent" class="preview-container">
        <div class="preview-toolbar">
          <span class="file-name">{{ selectedFileName }}</span>
        </div>
        <div class="preview-content">
          <template v-if="isImageFile">
            <img :src="selectedFileContent" alt="Image Preview" class="image-preview" />
          </template>
          <template v-else-if="isDocxFile">
            <!-- Use VueOfficeDocx for .docx file preview -->
            <VueOfficeDocx :src="selectedFileContent" />
          </template>
          <template v-else-if="isPdfFile">
            <!-- Use iframe for .pdf file preview -->
            <iframe :src="selectedFileContent" width="100%" height="100%" style="border: none;"></iframe>
          </template>
          <template v-else-if="isExcelFile">
            <!-- Use VueOfficeExcel for .xlsx file preview -->
            <VueOfficeExcel :src="selectedFileContent" />
          </template>
          <template v-else>
            <pre class="file-content">{{ selectedFileContent }}</pre>
          </template>
        </div>
      </div>
      <div v-else class="empty-tip">请选择一个文件进行预览</div>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { FolderOutlined, FileOutlined } from '@ant-design/icons-vue';
  import VueOfficeDocx from '@vue-office/docx'; // Import VueOfficeDocx
  import VueOfficeExcel from '@vue-office/excel'; // Import VueOfficeExcel

  console.log('index.vue');

  // 状态管理
  const fileTree = ref([]);
  const expandedKeys = ref([]);
  const selectedKeys = ref([]);
  const selectedFileContent = ref('');
  const selectedFileName = ref('');
  const isImageFile = ref(false);
  const isDocxFile = ref(false); // Add state for docx file detection
  const isPdfFile = ref(false); // Add state for pdf file detection
  const isExcelFile = ref(false); // Add state for excel file detection
  const isLoading = ref(false); // Add loading state

  // 转换文件结构为树形数据
  async function createTreeEntry(handle, path = '') {
    const node = {
      title: handle.name,
      key: path + handle.name,
      isDir: handle.kind === 'directory',
      children: [],
      handle: handle,
    };

    if (handle.kind === 'directory') {
      try {
        const entries = [];
        for await (const [name, entry] of handle.entries()) {
          entries.push([name, entry]);
        }

        // Sort entries alphabetically by name
        entries.sort(([nameA], [nameB]) => nameA.localeCompare(nameB));

        for (const [, entry] of entries) {
          const childNode = await createTreeEntry(entry, node.key + '/');
          node.children.push(childNode);
        }
      } catch (error) {
        console.error('Error reading directory:', error);
      }
    }

    return node;
  }

  // 读取整个目录结构
  async function readDirectory(dirHandle) {
    const tree = [];
    try {
      const entries = [];
      for await (const [name, entry] of dirHandle.entries()) {
        entries.push([name, entry]);
      }

      // Sort entries alphabetically by name
      entries.sort(([nameA], [nameB]) => nameA.localeCompare(nameB));

      for (const [, entry] of entries) {
        const node = await createTreeEntry(entry);
        tree.push(node);
      }
    } catch (error) {
      console.error('Error reading directory:', error);
    }
    return tree;
  }

  // 选择文件夹
  async function selectDirectory() {
    try {
      isLoading.value = true; // Start loading
      const dirHandle = await window.showDirectoryPicker();
      const tree = await readDirectory(dirHandle);

      // Clear and update state only after successful selection
      fileTree.value = tree;
      expandedKeys.value = tree.map((node) => node.key); // Expand all nodes
      selectedKeys.value = [];
      selectedFileContent.value = '';
      selectedFileName.value = '';
    } catch (error) {
      console.log('用户取消选择');
    } finally {
      isLoading.value = false; // Stop loading
    }
  }

  // 处理树节点展开
  async function handleExpand(keys) {
    expandedKeys.value = keys;
  }

  // 处理文件选择
  async function handleSelect([key]) {
    const findNode = (nodes) => {
      for (const node of nodes) {
        if (node.key === key) return node;
        if (node.children) {
          const found = findNode(node.children);
          if (found) return found;
        }
      }
    };

    const node = findNode(fileTree.value);
    if (node && !node.isDir) {
      selectedKeys.value = [key];
      selectedFileName.value = node.title;

      const file = await node.handle.getFile();
      const fileType = file.type;

      if (fileType.startsWith('image/')) {
        isImageFile.value = true;
        isDocxFile.value = false;
        isPdfFile.value = false;
        isExcelFile.value = false;
        selectedFileContent.value = URL.createObjectURL(file);
      } else if (file.name.endsWith('.docx')) {
        isDocxFile.value = true;
        isImageFile.value = false;
        isPdfFile.value = false;
        isExcelFile.value = false;
        selectedFileContent.value = URL.createObjectURL(file); // Use URL for VueOfficeDocx
      } else if (file.name.endsWith('.pdf')) {
        isPdfFile.value = true;
        isImageFile.value = false;
        isDocxFile.value = false;
        isExcelFile.value = false;
        selectedFileContent.value = URL.createObjectURL(file); // Use URL for PDF
      } else if (file.name.endsWith('.xlsx')) {
        isExcelFile.value = true;
        isImageFile.value = false;
        isDocxFile.value = false;
        isPdfFile.value = false;
        selectedFileContent.value = URL.createObjectURL(file); // Use URL for VueOfficeExcel
      } else {
        isImageFile.value = false;
        isDocxFile.value = false;
        isPdfFile.value = false;
        isExcelFile.value = false;
        selectedFileContent.value = await file.text();
      }
    }
  }

  // 样式部分
  const themeVars = {
    'sider-bg': '#f5f5f5',
    'content-bg': '#ffffff',
    'tree-node-hover-bg': '#e8e8e8',
    'file-content-bg': '#1e1e1e',
    'text-color': '#333',
  };
</script>

<style scoped>
  .container {
    height: 100vh;
    background: v-bind('themeVars.content-bg');
  }

  .sider {
    border-right: 1px solid #ddd;
    background: v-bind('themeVars.sider-bg') !important;
  }

  .toolbar {
    padding: 12px;
    border-bottom: 1px solid #eee;
  }

  .tree-node {
    display: flex;
    align-items: center;
    padding: 4px 0;
  }

  .tree-node :deep(.anticon) {
    margin-right: 8px;
    color: #6699cc;
  }

  .tree-node :deep(.ant-spin) {
    margin-left: 8px;
  }

  .content {
    padding: 16px;
    background: v-bind('themeVars.content-bg');
  }

  .preview-container {
    height: 100%;
    background: v-bind('themeVars.file-content-bg');
    color: #d4d4d4;
    border-radius: 4px;
    overflow: hidden;
    display: flex;
    flex-direction: column; /* Ensure file name is at the top */
  }

  .preview-toolbar {
    padding: 8px 16px;
    background: #252526;
    border-bottom: 1px solid #333;
  }

  .file-name {
    color: #fff;
  }

  .preview-content {
    flex: 1; /* Take remaining space for content */
    display: flex;
    justify-content: center;
    align-items: flex-start; /* Align content to the top */
    overflow-y: auto; /* Enable vertical scrolling */
    padding: 16px; /* Add padding for better readability */
  }

  .file-content {
    padding: 16px;
    margin: 0;
    font-family: Monaco, monospace;
    font-size: 12px;
    line-height: 1.5;
    white-space: pre-wrap;
  }

  .image-preview {
    max-width: 100%;
    max-height: 100%;
    display: block;
    margin: auto;
  }

  .empty-tip {
    color: v-bind('themeVars.text-color');
    padding: 24px;
    text-align: center;
  }
</style>
