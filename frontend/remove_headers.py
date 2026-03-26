import os
import re

files = [
    "src/views/Home.vue",
    "src/views/ProjectDetail.vue",
    "src/views/AdminUsers.vue",
    "src/views/SponsorProjects.vue",
    "src/views/UserOrders.vue"
]

header_regex = re.compile(r'<header class="header">.*?</header>\s*', re.DOTALL)
header_css_regex = re.compile(r'\.header\s*{[^}]*}\s*', re.DOTALL)
logo_css_regex = re.compile(r'\.logo\s*{[^}]*}\s*', re.DOTALL)
nav_links_css_regex = re.compile(r'\.nav-links\s*{[^}]*}\s*', re.DOTALL)

for file in files:
    filepath = os.path.join("/Users/bytedance/Workspace/05_Code/FundTogether/frontend", file)
    if os.path.exists(filepath):
        with open(filepath, "r", encoding="utf-8") as f:
            content = f.read()
        
        content = header_regex.sub("", content)
        content = header_css_regex.sub("", content)
        content = logo_css_regex.sub("", content)
        content = nav_links_css_regex.sub("", content)
        
        with open(filepath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Processed {file}")

