export interface ChatRoom {
  id: number;
  name: string;
  type: 'private' | 'group';
  createdDate: Date;
  createdBy: number;
  members: ChatMember[];
  messages: ChatMessage[];
}

export interface ChatMember {
  id: number;
  chatRoomId: number;
  userId: number;
  joinedDate: Date;
  user?: any;
}

export interface ChatMessage {
  id: number;
  chatRoomId: number;
  senderId: number;
  content: string;
  sentDate: Date;
  isRead: boolean;
  sender?: any;
}