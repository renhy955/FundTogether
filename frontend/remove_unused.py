import os
import re

# Home.vue
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/Home.vue", "r", encoding="utf-8") as f:
    content = f.read()
content = re.sub(r'const getRoleName = \(role: number\)[^{]*{[^}]*}', '', content)
content = re.sub(r'const logout = \(\) => {[^}]*}', '', content)
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/Home.vue", "w", encoding="utf-8") as f:
    f.write(content)

# AdminUsers.vue
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/AdminUsers.vue", "r", encoding="utf-8") as f:
    content = f.read()
content = re.sub(r'const router = useRouter\(\)', '', content)
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/AdminUsers.vue", "w", encoding="utf-8") as f:
    f.write(content)

# SponsorProjects.vue
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/SponsorProjects.vue", "r", encoding="utf-8") as f:
    content = f.read()
content = re.sub(r'const router = useRouter\(\)', '', content)
with open("/Users/bytedance/Workspace/05_Code/FundTogether/frontend/src/views/SponsorProjects.vue", "w", encoding="utf-8") as f:
    f.write(content)

