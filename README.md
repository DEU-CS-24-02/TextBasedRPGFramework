<h1>Text4Adventure</h1>

Text4Adventure은 자바로 제작된 텍스트 기반 RPG 프레임워크입니다.

<h2>기능</h2>

- 기 제작된 시나리오를 실행할 수 있습니다.
- 대부분의 데이터가 시나리오를 이용하여 쉽게 추가, 제거 할 수 있습니다.
- 여러개의 시나리오를 묶어 한번에 실행 가능합니다.

<h2>씬(Scene) 제작 방법</h2>

Scene은 시나리오의 요소 중 하나로, 텍스트가 표시되는 각 장을 뜻합니다. 이 씬들이 모여 시나리오를 구성하며, 씬은 엔티티, 플레이어, 아이템을 관리하고 구성 할 수 있습니다.

```json
{
  "VERSION_DO_NOT_TOUCH_THIS": 1,
  "comment_name": "name 키는 사용자에게 보여줄 시나리오의 이름을 결정합니다.",
  "name": "행상인",
  "comment_id": "id 키는 시스템적으로 인식되는 문자입니다. scenarioId:scene_name 으로 구성하는 것을 추천드리며, a-z,0-9,-_ 만 사용 가능합니다.",
  "id": "my_scenario:shopkeeper_meet",
  "comment_condition": "condition은 해당 scene 이 등장하기 위한 조건을 의미합니다.",
  "condition": {
    "comment_condition": "condition 은 if, never, always 로 나누어집니다.",
    "condition": "if",
    "comment_operator": "below(>=), more(<=), lower(>), over(<), eqauals(==), has(item, player tag)",
    "operator": "more",
    "value": 10,
    "unit": "karma",
    "comment_weight": "weight은 https://dev.to/jacktt/understanding-the-weighted-random-algorithm-581p 을 이용하여 실행됩니다. -1 은 조건을 만족하면 무조건 실행됩니다.",
    "weight": 1000
  },
  "comment_lore": "lore는 리스트로 정의되어 있습니다. lore의 인덱스는 줄 바꿈을 의미합니다.",
  "lore": [
    {
      "text": "험난한 여정이 계속되는 가운데, 당신은 외진 길에서 상인을 만났습니다."
    },
    "",
    {
      "condition": {
        "comment_condition": "condition 은 if, never, always 로 나누어집니다.",
        "condition": "if",
        "comment_operator": "below(>=), more(<=), lower(>), over(<), eqauals(==), has(Item, player Tag)",
        "operator": "more",
        "value": 10,
        "unit": "karma"
      },
      "text": "그는 당신을 보고, 싸게 해줄태니 아이템을 조금 보고 가라고 합니다."
    },
    {
      "condition": {
        "comment_condition": "condition 은 if, never, always 로 나누어집니다.",
        "condition": "if",
        "comment_operator": "below(>=), more(<=), lower(>), over(<), eqauals(==), has(only Item)",
        "operator": "below",
        "value": -10,
        "unit": "karma"
      },
      "text": "그는 당신을 보더니 기겁하고는 눈치를 보며, 아이템을 조금 보고 갈 생각이 있냐 말합니다."
    },
    {
      "text": ""
    },
    {
      "text": "당신은 어느정도 흥미가 당겨, 물품을 보기 시작했습니다."
    }
  ],
  "comment_decision": "decision은 선택지로 구성되어있습니다. 선택지에는 선택 가능한 조건을 설정 할 수 있습니다.",
  "decision": [
    {
      "comment_condition": "if, none 이 존재합니다. if는 unit, operator, value가 존재해야합니다.",
      "condition": "if",
      "comment_unit": "gold, health, armor, damage",
      "unit": "gold",
      "comment_operator": "below(>=), more(<=), lower(>), over(<), eqauals(==), has(only Item)",
      "operator": "more",
      "value": 1500,
      "then": {
        "comment_action": "action은 list로 구성되어있으며, 인덱스 순서대로 실행됩니다.",
        "action": [
          {
            "comment_type": "type 은 give, take, goto, print, disable_decision 으로 구성되어있습니다. give, take는 unit과 value가 구성되어야하며, goto는 이동할 신의 이름이 존재해야합니다. print는 text 란이 존재해야합니다. 문자열이여야합니다.",
            "comment_type_2": "decision 은 decisions 값으로 리스트 값을 받으며, 정수형이여야합니다. 엔티티와 조우하려면 encounter, value : entityId 로 하면됩니다.",
            "type": "take",
            "unit": "gold",
            "value": 1500
          },
          {
            "type": "give",
            "unit": "item",
            "item": "my_scenario:iron_sword",
            "value": 1
          },
          {
            "type": "print",
            "text": [
              "당신은 옳은 선택을 했다고 생각하며, 철 검을 구매합니다."
            ]
          },
          {
            "type": "disable_decision",
            "decisions": [
              0
            ]
          }
        ]
      }
    },
    {
      "then": {
        "action": [
          {
            "type": "print",
            "text": [
              "돈이 없기때문인지, 마음에 안들어서인지는 모르겠으나, 당신은 행상인을 뒤로두고 발길을 재촉합니다."
            ]
          },
          {
            "type": "goto",
            "comment_scene": "정해진 씬이 없다면 random으로 작성하세요.",
            "scene": "random"
          }
        ]
      }
    }
  ]
}
```

TODO : conditions

Scene은 시스템적으로 다시 보여주는 것이 아니라면(goto문) 더이상 나타나지 않습니다.

<h2>엔티티 제작 방법</h2>

엔티티는 아이템 다음으로 로드됩니다. 엔티티 파일은 무조건
scenario/entity/
에 존재해야합니다.

```json
{
  "comment_name": "사용자에게 보여줄 이름을 결정하는 부분입니다.",
  "name": "슬라임",
  "comment_id": "시스템적으로 엔티티를 식별할 아이디입니다. a-z,0-9,-_ 만 가능합니다.",
  "health": 5,
  "damage": 1,
  "defense": 0,
  "description": [
    "평범한 슬라임이다.",
    "",
    "약해보인다."
  ],
  "comment_type": "적대적(hostile), 중립(netural), 우호(friendly) 가 있으며, 이에 따라 조우 시 가능한 키워드가 달라집니다. ",
  "type": "hostile",
  "tags": [
    "my_scenario:slime_family",
    "my_scenario:tameable"
  ]
}
```

<h2>아이템</h2>

아이템은 scenario/item 에 존재해야합니다.

```json
{
  "name": "조잡한 검",
  "id": "my_scenario:simple_sword",
  "damage": 2,
  "defense": 0,
  "comment_slot": "slot은 착용가능 부위를 뜻합니다. head, chestplate, leggings, boots, one_handed, two_handed 로 구분됩니다.",
  "comment_slot_2" : " one_handed 는 두개를 낄 수 있고, two_handed는 한개만 낄수있습니다.",
  "comment_durability": "durability 는 내구도를 의미합니다. one_handed, two_handed는 공격 시, ",
  "comment_durability_2" : "head,chestplate,leggings,boots 는 피격 시 내구도가 줄어들며, 공란으로 두면 내구도는 무제한으로 취급됩니다.",
  "components" : {
    "damage" : 2,
    "defense" : 0,
    "equipable" : ["one_handed"],
    "durability" : 10
  }
}
```

<h2>태그</h2>

태그는 SceneTag, EntityTag로 나누어집니다.

<h3>Scene Tag</h3>

Selector에서 사용됩니다.
scenario/tag/scene 에 존재해야합니다.

```json
{
  "name": "불한당",
  "id": "my_scenario:scoundrel"
}
```

<h3>Entity Tag</h3>

Condition 등에 사용됩니다.

```json
{
  "name": "전쟁광",
  "id": "my_scenario:war_dog",
  "comment_type": "가능한 종류 : entity(플레이어를 포함한 모든 엔티티), non_player(플레이어를 제외한 모든 엔티티), player",
  "type": "player",
  "description": "당신이 가는 길에는 전쟁이 있습니다. 오늘도 당신은 텅빈 눈과 함께 전장으로 달려갑니다. 당신의 여정 너머에는 무엇이 있을까요?"
}
```
