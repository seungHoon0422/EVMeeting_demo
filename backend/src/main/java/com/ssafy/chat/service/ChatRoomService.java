package com.ssafy.chat.service;

import com.ssafy.chat.db.entity.ChatRoom;
import com.ssafy.chat.db.entity.Message;
import com.ssafy.chat.db.repository.ChatroomRepository;
import com.ssafy.chat.db.repository.MessageRepository;
import com.ssafy.chat.model.ChatRoomVO;
import com.ssafy.user.db.entity.User;
import com.ssafy.user.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatRoomService implements IChatRoomService {


    @Autowired
    ChatroomRepository chatroomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Override
    public long createRoom(ChatRoom newRoom) {
        newRoom.setRecentMessageTime(LocalDateTime.now().toString());
        try {
            ChatRoom chatRoom = chatroomRepository.save(newRoom);
            return chatRoom.getId();
        } catch (Exception e) {
            ChatRoom chatRoom = chatroomRepository.existChatroomByUserIds(newRoom.getUserid1(), newRoom.getUserid2());
            return chatRoom.getId();
        }

//        ChatRoom chatRoom = chatroomRepository.existChatroomByUserIds(newRoom.getUserid1(), newRoom.getUserid2());
//        if(chatRoom == null) {
//            chatRoom = chatroomRepository.existChatroomByUserIds(newRoom.getUserid2(), newRoom.getUserid1());
//            if(chatRoom == null) {
//                System.out.println("Create Room");
//                return chatroomRepository.save(newRoom).getId();
//            }
//        }
//        System.out.println("Exist Room");
//        return chatRoom.getId();
    }
    @Override
    public long findChatRoomByUsers(long user1, long user2) {
        return chatroomRepository.existChatroomByUserIds(user1, user2).getId();
    }
    @Override
    public List<ChatRoom> getAllChatRooms() {
        return chatroomRepository.findAll();
    }
    @Override
    public ChatRoom getChatRoomByChatRoomId(long id) {
        return chatroomRepository.findById(id).orElse(null);
    }

    @Override
    public List<ChatRoomVO> findChatRoomByUserid(long id) {

        List<ChatRoom> chatRooms = chatroomRepository.findChatRoomByUserid(id);
        Collections.sort(chatRooms, new Comparator<ChatRoom>() {
            @Override
            public int compare(ChatRoom o1, ChatRoom o2) {
                if(o1.getRecentMessageTime()!=null && o2.getRecentMessageTime()!=null) {
                    return o2.getRecentMessageTime().compareTo(o1.getRecentMessageTime());
                } else {
                    return (int) (o1.getId() - o2.getId());
                }
            }
        });

        List<ChatRoomVO> chatroomResult = new ArrayList<>();
        for(ChatRoom room : chatRooms) {
            if(!room.getAlive()) continue; // 삭제된 채팅방은 건너뛴다.
            ChatRoomVO vo = new ChatRoomVO();
            vo.setId(room.getId());
            vo.setUserid1(room.getUserid1());
            vo.setUserid2(room.getUserid2());
            vo.setAlive(room.getAlive());
            vo.setRecentMessage(room.getRecentMessage());
            vo.setRecentMessageTime(room.getRecentMessageTime());

            User user1 = userRepository.findById(room.getUserid1()).get();
            User user2 = userRepository.findById(room.getUserid2()).get();

            vo.setSenderId1(user1.getUserid());
            vo.setSenderId2(user2.getUserid());
            vo.setPhotoUrl1(user1.getPhoto());
            vo.setPhotoUrl2(user2.getPhoto());

            chatroomResult.add(vo);
        }
        return chatroomResult;
    }


    // 채팅방의 최신 메세지를 리턴
    @Override
    public void UpdateRecentMessage(Message message) {
        chatroomRepository.findById(message.getChatroomId())
                .map(chatroom -> {
                    chatroom.setRecentMessageId(message.getId());
                    chatroom.setRecentMessage(message.getContent());
                    chatroom.setRecentMessageTime(LocalDateTime.now().toString());
                    return chatroomRepository.save(chatroom);
                });
    }


    // 채팅방 삭제
    // 채팅방 table의 alive flag를 false로 변경
    @Override
    public void deleteRoom(long chatroomId) {
        chatroomRepository.findById(chatroomId)
                .map(chatroom -> {
                    chatroom.setAlive(false);
                    return chatroomRepository.save(chatroom);
                });
    }

    @Override
    public void removeRoom(long chatroomId) {
        chatroomRepository.deleteById(chatroomId);
        Optional<List<Message>> messages = messageRepository.findAllByChatroomId(chatroomId);
        messages.get().forEach(message -> {
            messageRepository.delete(message);
        });
    }

}
