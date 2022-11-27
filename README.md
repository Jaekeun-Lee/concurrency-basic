```
레이스 컨디션(Race Condition)
- 두 개의 프로세스/쓰레드가 하나의 자원을 접근하려고 할 때 경쟁하는 상태
```

# Application Level
- synchronized 키워드 사용
```java
@Transactional
public synchronized void decrease(Long id, Long quantity) {
    // TODO    
}
```

# Database Lock
### Optimistic Lock
- lock 을 걸지않고 문제가 발생할 때 처리합니다.
- 대표적으로 version column 을 만들어서 해결하는 방법이 있습니다.
### Pessimistic Lock (exclusive lock)
- 다른 트랜잭션이 특정 row 의 lock 을 얻는것을 방지합니다.
- A 트랜잭션이 끝날때까지 기다렸다가 B 트랜잭션이 lock 을 획득합니다.
- 특정 row 를 update 하거나 delete 할 수 있습니다.
- 일반 select 는 별다른 lock 이 없기때문에 조회는 가능합니다.
### Named Lock
- 이름과 함께 lock 을획득합니다. 해당 lock 은 다른세션에서 획득 및 해제가 불가능합니다.


# Redis Distributed Lock
## Redis Library   
재시도가 필요하지 않은 lock은 lettuce 활용  
재시도가 필요한 경우에는 redisson 를 활용

### Lettuce
- 구현이 간단하다
- spring data redis 를 이용하면 `lettuce`가 기본이기때문에 별도의 라이브러리를 사용하지 않아도 된다.
- spin lock 방식이기때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis 에 부하가 갈 수 있다.

### Redisson
- 락 획득 재시도를 기본으로 제공한다.
- `pub-sub` 방식으로 구현이 되어있기 때문에 lettuce 와 비교했을 때 redis 에 부하가 덜 간다.
- 별도의 라이브러리를 사용해야한다.
- lock 을 라이브러리 차원에서 제공해주기 떄문에 사용법을 공부해야 한다.



