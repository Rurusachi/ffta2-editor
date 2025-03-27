# Setup:
- Download the newest version here https://github.com/Rurusachi/ffta2-editor/releases/latest.
- Unpack the zip file to its own folder.
- Follow the instructions to install devkitPro (the NDS Development component is required) here https://devkitpro.org/wiki/Getting_Started.
- Find ndstool.exe in DevkitPro/tools/bin and copy it to the same folder as the FFTA2 Editor (next to ffta2-editor.exe).
- Launch the editor via ffta2-editor.exe.

# IMPORTANT:
- This editor is not compatible with any other FFTA2 editors. Once a rom has been modified with this editor, other editors will no longer work with that rom.
- This editor *shouldn't* affect the original rom, only the saved rom. However always make sure to keep a backup of the original rom just in case.
- There might be unknown bugs. Make backups of your work once in a while in case of an irreversible bug.

# Known Issues:
- When editing text the changes might not show up in all lists until selecting something else. This is purely a visual bug.
- The game uses images instead of text for job names in many places. This means that job names may not change everywhere. Particularly if you create new jobs.
- Text boxes in-game may not have the correct size. I haven't figured out exactly how the number of lines is calculated.


# How to make a new playable Job:
- Create a new Job.
- Set the Job's Ability Set to an existing one or create a new one. Make sure the Ability Set is marked as usable by the Job's race.
- Create a new Job Group or put it in an existing one (all jobs in a group must use the same Ability Set).
- Create a new Job Requirement.
- Set the Job's `Job Requirement` field (in the Other tab) to match the index of the one you just created.
- (Optional) (Apply the animation fix patch first if you haven't already) Create a new Unit Sprite by copying an existing one. Many units also have an in-water sprite, copy this too if needed. 

# How to make Al-Cid able to change jobs:
- Select the Agent Job and go to the Other tab and enable `Can change jobs`.
- Create a new Job Requirement. Set it as unique to Al-Cid.
- Set the Agent Job's `Job Requirement` field (in the Other tab) to match the index of the one you just created.