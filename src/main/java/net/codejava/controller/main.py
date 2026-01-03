import sys
import os

# Mock face recognition script
# The Java code reads src/main/resources/templates/out.txt
# and expects '1' at the 13th character (0-indexed).

out_dir = "src/main/resources/templates"
if not os.path.exists(out_dir):
    os.makedirs(out_dir)

out_file = os.path.join(out_dir, "out.txt")
with open(out_file, "w") as f:
    # 01234567890123
    # Result:      1
    f.write("Result is:   1")

print("Face verification successful (Mocked)")
