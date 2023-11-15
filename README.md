# subscribfy

DB Table Diagram

<img width="416" alt="diagram" src="https://github.com/youseonghyeon/subscribfy/assets/78669797/a99a68ae-45ee-4b08-88dd-c417d4e23119">

### 개발 중 어려웠던 부분과 그에 대한 해결방안은 다음과 같습니다.


- smart service 레이어를 만들지 않기 위한 계층 선택

어려웠던 부분: 검증 로직과 부가 로직의 위치를 어디에 두어야 하는지 결정하기 어려웠습니다.
해결방안: 필수 검증 로직은 서비스(service) 및 도메인(domain) 레이어에 배치하고, 기본 검증 로직은 
ArgumentResolver와 같은 기술을 활용하여 Controller에 배치하였습니다. 
역할에 따라 레이어를 분리하여 코드를 깔끔하게 구성하였습니다.


- 구독 구매 시 여러 옵션을 처리하는 로직의 복잡성

어려웠던 부분: 구독 옵션, 할인 정책 등 다양한 옵션을 처리하는 로직이 복잡해졌습니다.
해결방안: 각각의 옵션을 처리하는데 전략 패턴(Strategy Pattern)을 활용하여 코드를 모듈화하고 
유지보수성을 향상시켰습니다. 이로써 각 옵션을 독립적으로 관리하고 확장하기 용이한 시스템을 구축하였습니다.
