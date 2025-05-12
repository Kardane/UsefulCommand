# UsefulCommand
useful command for datapack/command makes in Minecraft
커맨드/데이터팩 제작자를 위한 유용한 커맨드추가모드

`/absorption (대상) get|add|set` 대상의 흡수 체력을 조작하거나 확인합니다.

`/fire (대상) get|add|set` 대상의 불 틱을 조작하거나 확인합니다.

`/freeze (대상) get|add|set` 대상의 냉기 틱(가루눈효과)을 조작하거나 확인합니다.

`/nogravity (대상) on|off` 대상의 중력을 활성화/비활성화 합니다.

`/invulnerable (대상) on|off` 대상을 무적으로 만들거나 해제합니다.

`/motion (대상) add|set|forward` 대상에게 모션을 부여합니다. forward는 바라보는 방향*후속 변수값으로 모션값을 부여합니다.

`/player_abilities fly|flyspeed|walkspeed|instantBreak|allowBuild` 플레이어의 능력을 조작합니다. \n비행여부,비행속도,걷는속도,크리에이티브 블록파괴여부,블록설치여부를 설정합니다. 맨뒤 옵션 2개는 실험적입니다.

`/heal (대상)`엔티티를 치유합니다.

`/food (대상) hunger|saturation add|get|set` 플레이어의 허기/포만감을 조절합니다.

`/fallfly` 플레이어를 겉날개 활공 상태로 만듭니다. 반복적으로 사용하는 것을 권장합니다.

`/cooldown (대상) (item) (cooldown)` 아이템에게 재사용 대기시간을 부여합니다. (엔더 진주/후렴과/방패의 재사용대기시간과 같은것입니다.)

`/motionupdate (대상)` 대상의 Motion값을 업데이트시킵니다. `/data merge`로 플레이어의 Motion값을 변경시키고 연이어 사용하세요.

`/explosion (위치) (위력) (폭발시킨 대상) (화염) (폭발종류)` 특정위치에 폭발을 일으킵니다.

