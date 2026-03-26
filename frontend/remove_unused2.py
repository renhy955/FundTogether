import os
import re

# Home.vue
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/Home.vue", "r", encoding="utf-8") as f:
    content = f.read()
content = re.sub(r"import \{ useUserStore \} from '\.\./store/user'", '', content)
content = re.sub(r"import \{ ElMessage \} from 'element-plus'", '', content)
content = re.sub(r'const userStore = useUserStore\(\)', '', content)
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/Home.vue", "w", encoding="utf-8") as f:
    f.write(content)

# AdminUsers.vue
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/AdminUsers.vue", "r", encoding="utf-8") as f:
    content = f.read()
content = re.sub(r"import \{ useRouter \} from 'vue-router'", '', content)
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/AdminUsers.vue", "w", encoding="utf-8") as f:
    f.write(content)

# SponsorProjects.vue
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/SponsorProjects.vue", "r", encoding="utf-8") as f:
    content = f.read()
content = re.sub(r"import \{ useRouter \} from 'vue-router'", '', content)
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/SponsorProjects.vue", "w", encoding="utf-8") as f:
    f.write(content)

