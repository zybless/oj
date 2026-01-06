<template>
  <mavon-editor
    :autofocus="false"
    v-model="value"
    codeStyle="arduino-light"
    :toolbarsFlag="false"
    :subfield="false"
    defaultOpen="preview"
    :boxShadow="false"
    previewBackground="#fff"
  >
  </mavon-editor>
</template>
<script>
import { addCodeBtn } from "@/common/codeblock";
export default {
  name: "EditorShow",
  props: {
    value: {
      type: String,
      default: "",
    },
  },
  mounted() {
    setTimeout(() => {
      this.$nextTick((_) => {
        if (this.value && this.value.indexOf("```") >= 0) {
          addCodeBtn();
        }
      });
    }, 200);
  },
};
</script>
<style>
.auto-textarea-wrapper .auto-textarea-block {
  white-space: pre-wrap !important;
}
</style>
