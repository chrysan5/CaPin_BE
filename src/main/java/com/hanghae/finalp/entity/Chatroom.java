package com.hanghae.finalp.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom {

    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;

    private String chatroomTitle;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<ChatMember> chatMembers = new ArrayList<>();

//    @OneToMany(mappedBy = "chatroom")
//    private List<MemberGroup> memberGroups = new ArrayList<>();

    //========================================생성자=============================================//
    private Chatroom(String chatroomTitle) {
        this.chatroomTitle = chatroomTitle;
    }

    //========================================생성 편의자=============================================//
    public static Chatroom createChatroomByGroup(String chatroomTitle, MemberGroup memberGroup) {
        Chatroom chatroom = new Chatroom(chatroomTitle);

        memberGroup.setChatroom(chatroom);
        return chatroom;
    }

    public static Chatroom createChatroomByMember(String chatroomTitle, Member member1, Member member2) {
        Chatroom chatroom = new Chatroom(chatroomTitle);

        ChatMember chatMember1 = ChatMember.createChatMember(member1, chatroom); //createChatMember이 그냥 ChatMember생성자와 같다고 보면됨
        ChatMember chatMember2 = ChatMember.createChatMember(member2, chatroom); //=> chatMember1,2를 만들어준다(chatMember에 member와 chatroom이 들어간(지정된) 상태)
                                                                                //=> 새 chatroom 생성시 chatMember을 지정해줌
        chatroom.getChatMembers().add(chatMember1); //chatMember1,2를 chatroom에 넣어서 chatroom 완성
        chatroom.getChatMembers().add(chatMember2);

        return chatroom;
    }
}