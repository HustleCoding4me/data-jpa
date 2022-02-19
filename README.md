#### JpaREpository 인터페이스 구성

![캡처](https://user-images.githubusercontent.com/37995817/154701822-bc37ea83-e989-485b-b3b6-bce55042e8c6.JPG)

> Repository, CrudRepository, PagingAndSortingRepository는 Common 라이브러리에 속해있다.
> 사용자의 DB, 환경에 따라서 변화할 수 있다.
> JPA에 특화시킨것이 JpaRepository이다. 

* 주요 메서드

| 메서드 명 | 내용 |
|---|:---:|
| `save(S)` | 새로운 Entity는 저장, 이미 있으면 mergy 한다. | 
| `delete(T)` | 엔티티 하나를 삭제한다. `EntityManager.remove()` |  
| `findById(ID)` | 엔티티 하나를 조회한다. `EntityManager.find()` |  
| `getOne(ID)` | 엔티티를 프록시로 조회한다. `EntityManager.getReference()` |  
| `findAll(...)` | 모든 엔티티를 조회한다. Sort, Paging 조건을 파라미터로 제공할 수 있다. |  

#### 검색 조건이 들어간 쿼리는 어떻게 처리해야 하는가? (기본 제공 인터페이스 이외)

> 쿼리 메소드 기능을 사용한다.

1. 메소드 이름으로 쿼리 생성

> ex) Member 엔티티에서 이름 'AAA'와 15살 이상인 경우를 List에 담아 오는 기능
   
   - 기존 일반 JPA에서의 구현
   
```java
public List<Member> findByusernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }
```

   - Spring Data JPA에서 구현
   
```java
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByusernameAndAgeGreaterThan(String username, int age);
}
```
> interface에 약속된 이름의 메서드를 제작하면 알아서 변환해서 같은 쿼리를 내준다. (GreaterThan은 > 부등호로 연결)

<table class="tableblock frame-all grid-all fit-content">
<caption class="title">Table 3. Supported keywords inside method names</caption>
<colgroup>
<col>
<col>
<col>
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Keyword</th>
<th class="tableblock halign-left valign-top">Sample</th>
<th class="tableblock halign-left valign-top">JPQL snippet</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Distinct</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findDistinctByLastnameAndFirstname</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>select distinct &#8230;&#8203; where x.lastname = ?1 and x.firstname = ?2</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>And</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByLastnameAndFirstname</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.lastname = ?1 and x.firstname = ?2</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Or</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByLastnameOrFirstname</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.lastname = ?1 or x.firstname = ?2</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Is</code>, <code>Equals</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByFirstname</code>,<code>findByFirstnameIs</code>,<code>findByFirstnameEquals</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.firstname = ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Between</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByStartDateBetween</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.startDate between ?1 and ?2</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>LessThan</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAgeLessThan</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age &lt; ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>LessThanEqual</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAgeLessThanEqual</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age &lt;= ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>GreaterThan</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAgeGreaterThan</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age &gt; ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>GreaterThanEqual</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAgeGreaterThanEqual</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age &gt;= ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>After</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByStartDateAfter</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.startDate &gt; ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Before</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByStartDateBefore</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.startDate &lt; ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>IsNull</code>, <code>Null</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAge(Is)Null</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age is null</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>IsNotNull</code>, <code>NotNull</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAge(Is)NotNull</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age not null</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Like</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByFirstnameLike</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.firstname like ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>NotLike</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByFirstnameNotLike</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.firstname not like ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>StartingWith</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByFirstnameStartingWith</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.firstname like ?1</code> (parameter bound with appended <code>%</code>)</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>EndingWith</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByFirstnameEndingWith</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.firstname like ?1</code> (parameter bound with prepended <code>%</code>)</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Containing</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByFirstnameContaining</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.firstname like ?1</code> (parameter bound wrapped in <code>%</code>)</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>OrderBy</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAgeOrderByLastnameDesc</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age = ?1 order by x.lastname desc</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Not</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByLastnameNot</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.lastname &lt;&gt; ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>In</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAgeIn(Collection&lt;Age&gt; ages)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age in ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>NotIn</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByAgeNotIn(Collection&lt;Age&gt; ages)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.age not in ?1</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>True</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByActiveTrue()</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.active = true</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>False</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByActiveFalse()</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where x.active = false</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>IgnoreCase</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>findByFirstnameIgnoreCase</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>… where UPPER(x.firstname) = UPPER(?1)</code></p></td>
</tr>
</tbody>
</table>

참고 <https://spring.io/projects/spring-data-jpa>



2. 메소드 이름으로 JPA NamedQuery 호출 

> Member Entity 위에 @NamedQuery로 name, query 선언한다.

```java
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
public class Member {
```

> JpaRepository를 상속한 interface에서 @Query 문구를 붙여준다.

```java
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByusernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
```
> 실행 순서
>> @Query를 없애도 Named 규칙을 먼저 찾아 실행한다. (Entity.선언한 메서드 이름)
>> Named 쿼리가 없으면 메서드내임생성규칙으로 실행한다.

> 장단점
>> 장점
>>> @NamedQuery 어노테이션 하위에 query= ""로 선언하면, compile 단계에서 오류를 잡아준다.
>>> em.createQuery 시에는 모두 String이라 해당 기능이 실행되기 전까지 compile 단계에서 에러를 잡아주지 못함.
```java
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.usernamefuc = :username" //username 에 오타가 난 모습
)
public class Member {
```
![image](https://user-images.githubusercontent.com/37995817/154790773-2176dda4-0f4b-40ce-9057-60da72728849.png)


>> 단점
>>> 3번 방법인 직접 Query 사용이 막강해서 잘 쓰이지 않는다.

3. 인터페이스 메서드에 직접 @Query로 실행 JPQL 삽입

```java
@Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
```

### @Query로 Dto, 특정 table 값 가져오기
> 기본적인 JPQL 값을 선언해주는 것은 똑같다.
> Dto class를 생성해서 생성자 선언, @Quert("JPQL")안에 new로 선언해주며 Dto 생성해주면서 가져오면 된다.
> 특정 Entity의 value를 가져오는 것은 JPQL에서 해당 value 가져오게 선언, 가져올 List나 해당 Value에 맞는 속성을 적어주면 된다.

```java
   //Member의 username만 가져오는 경우. List<String> 으로 username 속성을 맞춰줌
    @Query("select m.username from Member m")
    List<String> findUsernameList();
   
   
   //MemberDto로 Mapping 하여 받는 경우
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
```

> @Query의 파라미터 바인딩은 @Param을 삽입해주는 것으로 해결한다.

```java
  @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);//@Param으로 username을 삽입해주는 경우. JPQL의 :username이 선언되어 잇을 것이다.
```
> 이 파라미터로 이름 뿐만 아니라 Collection 삽입도 가능하다. (In절 같은)

```java
    @Query("select m from Member m where username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);
```

> Test모습

```java
    @Test
    public void findByNames() throws Exception {
        //given
        getTestMembers(new Member("AAA", 10), new Member("AAA", 20));
        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB")); //리스트로 넣어주는 모습
        //then
        for (Member member : result) {
            System.out.println("members : " +  member);
        }
    }
```

> 장단점
>> 장점
>>> 이름이 없는 Named쿼리라 생각하면 된다. 컴파일 시점에 @Query에 멤버변수 오타나 이런 것들을 잡아준다.


#### SpringDataJPA 유연한 ReturnType으로 파라미터 받기

> SpringDataJPA는 선언한 반환타입으로 받을 수 있다.

```java
   List<Member> findListByUsername(String username);//컬렉션
    Member findMemberByUsername(String username);//단건
    Optional<Member> findOptionalByUsername(String username);//단건Optional
```

```java

@Test
    public void returnType() throws Exception {
        getTestMembers(new Member("AAA", 10), new Member("BBB", 20));

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        
        Member aaa1 = memberRepository.findMemberByUsername("AAA");

        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");
        

```
> 예외 Return시
1. List로 Return 받는 경우
   - 좋은 점은 `null`일 경우(유저가 없을 겨우) `EmptyColection`을 return 해준다.
   - 절대 `null`로 return하지 않는다. != null체크 안해도 됨
2. 단건 조회, Optional 단건 조회
  - 단건 조회일 경우 없으면 `null`이다.
  - `JPA`는 결과가 없을 경우 `noResultAcception`이 뜬다.
  - `SpringDataJPA`는 그냥 `null`로 반환 해버린다.
  - 사실 그냥 `Optional`로 받으면 된다. (Optional이 나오기 전까지 null로 return이 낫다. 그냥 Exception으로 중단이 낫다 분쟁거리였음)
> ※ 만일 단일 조회인데 2건 이상 querty에서 return이 된다면?

  - 만약 한 건 조회인데 2건이상이 조회가 될경우 `SpringFrameWork`의 `IncorrectResultSizeDataAccessException`이 터진다.
  - 원래 `NonUniqueDataException`이 터지지지만, `SpringFrameWork`의 예외로 변환해서 준다.
  - `why?` `client`가 여러 `DB`를 사용해도 `SpringFrameWork`가 한번 감싸서 주면
  - 동일한 상황에 동일한 `Exception`이 넘어올텐데데 그럼 개발자가 동일한 처리를 유지해도 되어서 좋다.


<table class="tableblock frame-all grid-all stretch">
<caption class="title">Table 11. Query return types</caption>
<colgroup>
<col style="width: 25%;">
<col style="width: 75%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top">Return type</th>
<th class="tableblock halign-left valign-top">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>void</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">Denotes no return value.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">Primitives</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">Java primitives.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">Wrapper types</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">Java wrapper types.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>T</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A unique entity. Expects the query method to return one result at most. If no result is found, <code>null</code> is returned. More than one result triggers an <code>IncorrectResultSizeDataAccessException</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Iterator&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">An <code>Iterator</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Collection&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A <code>Collection</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>List&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A <code>List</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Optional&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A Java 8 or Guava <code>Optional</code>. Expects the query method to return one result at most. If no result is found, <code>Optional.empty()</code> or <code>Optional.absent()</code> is returned. More than one result triggers an <code>IncorrectResultSizeDataAccessException</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Option&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">Either a Scala or Vavr <code>Option</code> type. Semantically the same behavior as Java 8’s <code>Optional</code>, described earlier.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Stream&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A Java 8 <code>Stream</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Streamable&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A convenience extension of <code>Iterable</code> that directy exposes methods to stream, map and filter results, concatenate them etc.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">Types that implement <code>Streamable</code> and take a <code>Streamable</code> constructor or factory method argument</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">Types that expose a constructor or <code>….of(…)</code>/<code>….valueOf(…)</code> factory method taking a <code>Streamable</code> as argument. See <a href="#repositories.collections-and-iterables.streamable-wrapper">Returning Custom Streamable Wrapper Types</a> for details.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">Vavr <code>Seq</code>, <code>List</code>, <code>Map</code>, <code>Set</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">Vavr collection types. See <a href="#repositories.collections-and-iterables.vavr">Support for Vavr Collections</a> for details.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Future&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A <code>Future</code>. Expects a method to be annotated with <code>@Async</code> and requires Spring’s asynchronous method execution capability to be enabled.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>CompletableFuture&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A Java 8 <code>CompletableFuture</code>. Expects a method to be annotated with <code>@Async</code> and requires Spring’s asynchronous method execution capability to be enabled.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>ListenableFuture</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A <code>org.springframework.util.concurrent.ListenableFuture</code>. Expects a method to be annotated with <code>@Async</code> and requires Spring’s asynchronous method execution capability to be enabled.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Slice&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A sized chunk of data with an indication of whether there is more data available. Requires a <code>Pageable</code> method parameter.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Page&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A <code>Slice</code> with additional information, such as the total number of results. Requires a <code>Pageable</code> method parameter.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>GeoResult&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A result entry with additional information, such as the distance to a reference location.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>GeoResults&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A list of <code>GeoResult&lt;T&gt;</code> with additional information, such as the average distance to a reference location.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>GeoPage&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A <code>Page</code> with <code>GeoResult&lt;T&gt;</code>, such as the average distance to a reference location.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Mono&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A Project Reactor <code>Mono</code> emitting zero or one element using reactive repositories. Expects the query method to return one result at most. If no result is found, <code>Mono.empty()</code> is returned. More than one result triggers an <code>IncorrectResultSizeDataAccessException</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Flux&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A Project Reactor <code>Flux</code> emitting zero, one, or many elements using reactive repositories. Queries returning <code>Flux</code> can emit also an infinite number of elements.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Single&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A RxJava <code>Single</code> emitting a single element using reactive repositories. Expects the query method to return one result at most. If no result is found, <code>Mono.empty()</code> is returned. More than one result triggers an <code>IncorrectResultSizeDataAccessException</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Maybe&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A RxJava <code>Maybe</code> emitting zero or one element using reactive repositories. Expects the query method to return one result at most. If no result is found, <code>Mono.empty()</code> is returned. More than one result triggers an <code>IncorrectResultSizeDataAccessException</code>.</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>Flowable&lt;T&gt;</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">A RxJava <code>Flowable</code> emitting zero, one, or many elements using reactive repositories. Queries returning <code>Flowable</code> can emit also an infinite number of elements.</p></td>
</tr>
</tbody>
</table>
